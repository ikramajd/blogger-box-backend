create table if not exists category (
    id uuid primary key,
    name varchar(100) not null
);

create table if not exists post (
    id uuid primary key,
    title varchar(500) not null,
    content text not null,
    created_date timestamp not null,
    category_id uuid not null references category(id)
);

insert into category (id, name) values
    ('a056ce20-d0f7-4c73-988a-35991d993dc6', 'Adoption'),
    ('c5cfcc72-23dc-4857-a8c5-3a6f0a578ecb', 'Children'),
    ('8707cb67-17e0-4125-9172-f035f1327c96', 'Love'),
    ('54b882cd-cc28-4872-97e5-3bbb14675ac1', 'Anxiety'),
    ('caa81120-9015-40f4-ae0f-8431bf627cd6', 'Coding'),
    ('c790063f-016f-43e2-815d-e7790aefe90c', 'Java'),
    ('b319a314-4edc-470e-8e6e-d11ca412b4c5', 'Travel')
on conflict (id) do nothing;

insert into post (id, title, content, created_date, category_id) values
    (
        '5284661c-683f-497d-8d41-0b84d52b5212',
        'Premier post local',
        'Ce post vient de ton backend Spring Boot local.',
        '2026-04-10T16:01:40.661Z',
        '54b882cd-cc28-4872-97e5-3bbb14675ac1'
    ),
    (
        '8e8910c2-5673-4481-97dc-00c9b614419f',
        'The Basics of Coding',
        'Introduction aux bases de la programmation avec Java et Angular.',
        '2026-03-26T10:39:42.581Z',
        'caa81120-9015-40f4-ae0f-8431bf627cd6'
    ),
    (
        '2f60d149-9f1f-4792-87fb-6d4a7f3ce055',
        'Preparing a Travel Blog',
        'Quelques idees pour ecrire un article de voyage.',
        '2026-02-20T06:37:17.748Z',
        'b319a314-4edc-470e-8e6e-d11ca412b4c5'
    )
on conflict (id) do nothing;
