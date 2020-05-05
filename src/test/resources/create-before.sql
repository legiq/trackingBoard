drop table if exists tickets_executors;
drop table if exists tickets;
drop table if exists users;
drop type if exists user_role;
drop type if exists ticket_status;
drop type if exists ticket_type;
drop type if exists ticket_component;

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
description varchar(300) not null,
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


insert into users (login, password, role, active) values
('user',1,'Developer'::user_role,true),
('qaUser', 1, 'QA'::user_role, true);

insert into tickets (label, description, creator_id, type, status, components, time, story_id) values
('task','info',1,'Task'::ticket_type,'ToDo'::ticket_status,'{Backend}'::ticket_component[],'2020-01-01',null),
('bug','info',1,'Bug'::ticket_type,'InProgress'::ticket_status,'{Frontend}'::ticket_component[],'2020-01-01',null);

insert into tickets_executors (ticket_id, executor_id) values
(1,1),
(2,2);