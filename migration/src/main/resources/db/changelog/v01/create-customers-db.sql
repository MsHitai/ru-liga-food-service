create sequence if not exists customers_seq;

create table if not exists customers
(
    id      bigint not null default nextval('customers_seq'),
    phone   varchar(80),
    email   varchar(512),
    address varchar(1000),
    constraint customers_pk primary key (id)
);

comment on table customers is 'Таблица клиентов';
comment on column customers.id is 'Идентификатор';
comment on column customers.phone is 'Номер телефона';
comment on column customers.email is 'Электронная почта';
comment on column customers.address is 'Адрес';
