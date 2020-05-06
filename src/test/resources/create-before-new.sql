delete from tickets_executors;
delete from tickets;
delete from users;

ALTER SEQUENCE seq RESTART;

insert into users (login, password, role, active) values
('user',1,'Developer'::user_role,true),
('qaUser', 1, 'QA'::user_role, true);

insert into tickets (label, description, creator_id, type, status, components, time, story_id) values
('task','info',1,'Task'::ticket_type,'ToDo'::ticket_status,'{Backend}'::ticket_component[],'2020-01-01',null),
('bug','info',1,'Bug'::ticket_type,'InProgress'::ticket_status,'{Frontend}'::ticket_component[],'2020-01-01',null);

insert into tickets_executors (ticket_id, executor_id) values
(1,1),
(2,2);