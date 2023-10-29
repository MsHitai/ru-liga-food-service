create sequence if not exists users_authorities_seq;

create table if not exists users_authorities
(
    id           bigint not null default nextval('users_authorities_seq'),
    user_id      bigint not null,
    authority_id bigint not null,
    constraint users_authorities_pk primary key (id),
    constraint users_to_authorities_fk foreign key (user_id) references users (id) on delete cascade,
    constraint authorities_to_users_fk foreign key (authority_id) references authorities (id) on delete cascade
);

comment on table users_authorities is 'Таблица прав и пользователей';
comment on column users_authorities.id is 'Идентификатор';
comment on column users_authorities.user_id is 'Ссылка на пользователя';
comment on column users_authorities.authority_id is 'Ссылка на права (роль)';