create sequence if not exists couriers_seq;

create table if not exists couriers
(
    id          bigint not null default nextval('couriers_seq'),
    phone       varchar(80),
    status      varchar(80),
    coordinates varchar(255),
    constraint couriers_pk primary key (id)
);

comment on table couriers is 'Таблица курьеров';
comment on column couriers.id is 'Идентификатор';
comment on column couriers.phone is 'Номер телефона';
comment on column couriers.status is 'Статус';
comment on column couriers.coordinates is 'Координаты';
