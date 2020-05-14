delete from tickets_executors;
delete from tickets;

alter table tickets add column number bigserial not null;