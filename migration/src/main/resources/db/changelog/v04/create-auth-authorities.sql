create sequence if not exists authorities_seq;

create table if not exists authorities
(
    id        bigint      not null default nextval('authorities_seq'),
    authority varchar(50) not null unique,
    constraint auth_authorities_pk primary key (id)
);

comment on table authorities is 'Таблица прав пользователей (роли)';
comment on column authorities.id is 'Идентификатор';
comment on column authorities.authority is 'Название роли, определяющее права';