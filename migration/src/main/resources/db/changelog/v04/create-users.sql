create table if not exists users
(
    username varchar(50)          not null
        primary key,
    password varchar(100)         not null,
    enabled  boolean default true not null
);

comment on table users is 'Таблица пользователей';
comment on column users.username is 'Имя пользователя, уникальное';
comment on column users.password is 'Пароль';
comment on column users.enabled is 'Доступен или заблокирован';