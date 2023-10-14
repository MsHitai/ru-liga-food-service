create sequence if not exists orders_seq;

create table if not exists orders
(
    id            bigint not null default nextval('orders_seq'),
    customer_id   bigint not null,
    restaurant_id bigint not null,
    status        varchar(80),
    courier_id    bigint,
    constraint orders_pk primary key (id),
    constraint customers_to_orders_fk foreign key (customer_id) references customers (id),
    constraint restaurants_to_orders_fk foreign key (restaurant_id) references restaurants (id),
    constraint couriers_to_orders_fk foreign key (courier_id) references couriers (id)
);

comment on table orders is 'Таблица заказов';
comment on column orders.id is 'Идентификатор';
comment on column orders.customer_id is 'Ссылка на идентификатор клиента';
comment on column orders.restaurant_id is 'Ссылка на идентификатор ресторана';
comment on column orders.status is 'Статус заказа';
comment on column orders.courier_id is 'Ссылка на идентификатор курьера';
