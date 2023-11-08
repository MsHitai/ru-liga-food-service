create table if not exists authorities
(
    username  varchar(50) not null
        references users,
    authority varchar(50) not null
        primary key
);

comment on table authorities is 'Таблица прав пользователей (роли)';
comment on column authorities.username is 'Ссылка на пользователей';
comment on column authorities.authority is 'Название роли, определяющее права';