package com.dauphine.blogger.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "Hello world API",
        description = "My first hello world endpoints"
)
public class HelloWorldController {

    @GetMapping("hello-world")
    @Operation(
            summary = "Hello by name endpoints",
            description = "Returns Hello {name}' by path variable"
    )
    public String helloWorld() {
        return "Hello World!";
    }


    @GetMapping("hello")
    public String helloByName(@RequestParam String name){
        return "Hello " + name;
    }

    @GetMapping("hello/{name}")
    public String hello(@PathVariable String name){
        return "Hello  " +name;
    }

}
