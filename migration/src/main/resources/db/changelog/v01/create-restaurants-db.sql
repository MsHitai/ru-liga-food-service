create sequence if not exists restaurants_seq;

create table if not exists restaurants
(
    id      bigint not null default nextval('restaurants_seq'),
    address varchar(1000),
    status  varchar(80),
    constraint restaurants_pk primary key (id)
);

comment on table restaurants is 'Таблица ресторанов';
comment on column restaurants.id is 'Идентификатор';
comment on column restaurants.address is 'Адрес';
comment on column restaurants.status is 'Статус';
