create sequence if not exists users_seq;

create table if not exists users
(
    id          bigint       not null default nextval('users_seq'),
    username    varchar(50)  not null unique,
    password    varchar(100) not null,
    enabled boolean default true not null ,
    constraint auth_users_pk primary key (id)
);

comment on table users is 'Таблица пользователей';
comment on column users.id is 'Идентификатор';
comment on column users.username is 'Имя пользователя, уникальное';
comment on column users.password is 'Пароль';
comment on column users.enabled is 'Доступен или заблокирован';