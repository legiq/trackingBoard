create type user_role as enum('Developer', 'QA', 'BA');
create type ticket_status as enum('ToDo', 'InProgress', 'InTest', 'Done');
create type ticket_type as enum('Task', 'Bug', 'Story');
create type ticket_component as enum('Frontend', 'Backend', 'DataScience', 'None');

create table users (
    id bigserial primary key,
    login varchar(128) not null,
    password varchar(128) not null,
    role user_role not null,
    active boolean not null
);


create table tickets (
    id bigserial primary key,
    creator_id bigint not null references users(id),
    label varchar(300) not null,
    description varchar(2048) not null,
    type ticket_type not null,
    status ticket_status not null,
    components ticket_component[] not null,
    time date not null,
    story_id bigint references tickets(id)
);

create table tickets_executors(
    id bigserial primary key,
    ticket_id bigint references tickets(id),
    executor_id bigint references users(id)
);