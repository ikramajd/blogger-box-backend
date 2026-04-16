# Layer-3 Lab Solution

This solution assumes Arista cEOS / EOS syntax.

## Assumptions

- OSPF process ID: `1`
- OSPF area: `0`
- EDGE ASN: `65000`
- ISP ASN: `65100`
- Loopbacks:
  - EDGE: `172.16.0.1/32`
  - ISP: `172.16.0.2/32`
  - R1: `172.16.0.3/32`
  - R2: `172.16.0.4/32`
- Replace placeholders like `<R1-EDGE-/30>` or `<EDGE-TO-ISP-IP>` with the actual addressing used in your lab.

Important: Tasks `3.6` and `3.7` require one extra step that is not stated explicitly in the lab text: EDGE must redistribute the BGP-learned CLIENT route back into OSPF, otherwise R1 and R2 will not know how to reach `10.3.10.0/24`.

## Task 1: OSPF on R1 and R2

### R1

```eos
ip routing

router ospf 1
   router-id 172.16.0.3
   network 172.16.0.3/32 area 0
   network <R1-EDGE-/30> area 0
   network <R1-R2-/30> area 0
   redistribute static
```

If `10.1.10.0/24` is directly connected on R1 instead of installed as a static route, use this instead of `redistribute static`:

```eos
router ospf 1
   network 10.1.10.0/24 area 0
```

### R2

```eos
ip routing

router ospf 1
   router-id 172.16.0.4
   network 172.16.0.4/32 area 0
   network <R2-EDGE-/30> area 0
   network <R1-R2-/30> area 0
   redistribute static
```

If `10.2.10.0/24` is directly connected on R2 instead of installed as a static route, use this instead of `redistribute static`:

```eos
router ospf 1
   network 10.2.10.0/24 area 0
```

### Verification for Task 1

On R1 and R2:

```eos
show ip ospf neighbor
show ip route ospf
show ip route 10.1.10.0/24
show ip route 10.2.10.0/24
```

From `SITE1`:

```bash
ping 10.2.10.10
```

From `SITE2`:

```bash
ping 10.1.10.10
```

Expected result: `SITE1` and `SITE2` can ping each other through `R1` and `R2`.

## Task 2: OSPF on EDGE

### EDGE

```eos
ip routing

router ospf 1
   router-id 172.16.0.1
   network 172.16.0.1/32 area 0
   network <EDGE-R1-/30> area 0
   network <EDGE-R2-/30> area 0
```

### Verification for Task 2

On EDGE:

```eos
show ip ospf neighbor
show ip route ospf
ping 172.16.0.3 source 172.16.0.1
ping 172.16.0.4 source 172.16.0.1
```

On R1:

```eos
ping 172.16.0.1 source 172.16.0.3
ping 172.16.0.4 source 172.16.0.3
```

On R2:

```eos
ping 172.16.0.1 source 172.16.0.4
ping 172.16.0.3 source 172.16.0.4
```

From `SITE1`:

```bash
ping 172.16.0.1
```

From `SITE2`:

```bash
ping 172.16.0.1
```

Expected result: EDGE, R1 and R2 all reach each other's loopbacks, and both site hosts reach EDGE loopback `172.16.0.1`.

## Task 3: eBGP on EDGE and ISP

### EDGE

This configuration advertises the two site LANs into BGP and injects the CLIENT LAN learned from BGP back into OSPF.

```eos
ip prefix-list SITE_ROUTES seq 10 permit 10.1.10.0/24
ip prefix-list SITE_ROUTES seq 20 permit 10.2.10.0/24
ip prefix-list CLIENT_ROUTE seq 10 permit 10.3.10.0/24

route-map OSPF_TO_BGP permit 10
   match ip address prefix-list SITE_ROUTES

route-map BGP_TO_OSPF permit 10
   match ip address prefix-list CLIENT_ROUTE

router bgp 65000
   router-id 172.16.0.1
   neighbor <EDGE-TO-ISP-IP> remote-as 65100
   redistribute ospf route-map OSPF_TO_BGP

router ospf 1
   redistribute bgp route-map BGP_TO_OSPF
```

### ISP

If `10.3.10.0/24` is directly connected on ISP, this is the cleanest configuration:

```eos
router bgp 65100
   router-id 172.16.0.2
   neighbor <ISP-TO-EDGE-IP> remote-as 65000
   network 10.3.10.0/24
```

If your lab specifically expects route redistribution instead of a BGP `network` statement, use:

```eos
ip prefix-list CLIENT_ROUTE seq 10 permit 10.3.10.0/24

route-map CLIENT_TO_BGP permit 10
   match ip address prefix-list CLIENT_ROUTE

router bgp 65100
   router-id 172.16.0.2
   neighbor <ISP-TO-EDGE-IP> remote-as 65000
   redistribute connected route-map CLIENT_TO_BGP
```

If the CLIENT subnet is static on ISP instead of connected, replace `redistribute connected` with `redistribute static`.

### Wireshark capture on EDGE

Capture on the EDGE interface facing ISP.

Display filter:

```text
tcp.port == 179 || bgp
```

You should see:

1. TCP three-way handshake
2. BGP `OPEN`
3. BGP `KEEPALIVE`
4. BGP `UPDATE`

### Verification for Task 3

On EDGE and ISP:

```eos
show ip bgp summary
show ip bgp
show ip route bgp
```

On EDGE:

```eos
show ip route 10.3.10.0/24
```

On R1 and R2:

```eos
show ip route 10.3.10.0/24
```

Expected routing table results:

- ISP learns `10.1.10.0/24` and `10.2.10.0/24` via eBGP from EDGE.
- EDGE learns `10.3.10.0/24` via eBGP from ISP.
- R1 and R2 learn `10.3.10.0/24` via OSPF from EDGE.

From `SITE1`:

```bash
ping 10.3.10.10
traceroute 10.3.10.10
```

Expected path:

```text
SITE1 -> R1 -> EDGE -> ISP -> CLIENT
```

From `SITE2`:

```bash
ping 10.3.10.10
traceroute 10.3.10.10
```

Expected path:

```text
SITE2 -> R2 -> EDGE -> ISP -> CLIENT
```

## Quick checklist

- `show ip ospf neighbor` is full between EDGE, R1 and R2.
- `show ip bgp summary` is established between EDGE and ISP.
- EDGE has BGP routes for `10.3.10.0/24`.
- ISP has BGP routes for `10.1.10.0/24` and `10.2.10.0/24`.
- R1 and R2 have OSPF routes for `10.3.10.0/24`.
- `SITE1` and `SITE2` both reach `10.3.10.10`.
