ALTER SEQUENCE tickets_id_seq RESTART WITH 10;

insert into users (id,login, password, role, active) values
(1,'user',1,'Developer'::user_role,true),
(2,'qaUser', 1, 'QA'::user_role, true),
(3,'admin', 1, 'Admin'::user_role, true);

insert into tickets (id,label, description, creator_id, type, status, components, time, story_id, number) values
(1,'task','info',1,'Task'::ticket_type,'ToDo'::ticket_status,'{Backend}'::ticket_component[],'2020-01-01',null,100),
(2,'bug','info',1,'Bug'::ticket_type,'InProgress'::ticket_status,'{Frontend}'::ticket_component[],'2020-01-01',3,101),
(3,'story','info',1,'Story'::ticket_type,'InTest'::ticket_status,'{Frontend}'::ticket_component[],'2020-01-01',null,102);

insert into tickets_executors (ticket_id, executor_id) values
(1,1),
(2,2),
(3,1);