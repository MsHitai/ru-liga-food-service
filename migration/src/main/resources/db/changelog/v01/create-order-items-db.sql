create sequence if not exists orders_items_seq;

create table if not exists order_items
(
    id                   bigint not null default nextval('orders_items_seq'),
    order_id             uuid   not null,
    restaurant_menu_item bigint not null,
    price                decimal(10, 2),
    quantity             int,
    constraint order_items_pk primary key (id),
    constraint order_items_to_orders_fk foreign key (order_id) references orders (id),
    constraint order_items_to_restaurant_items_fk foreign key (restaurant_menu_item) references restaurant_menu_items (id)
);

comment on table order_items is 'Таблица заказов блюд';
comment on column order_items.id is 'Идентификатор';
comment on column order_items.order_id is 'Ссылка на идентификатор заказа';
comment on column order_items.restaurant_menu_item is 'Ссылка на идентификатор блюда из меню ресторана';
comment on column order_items.price is 'Стоимость заказа';
comment on column order_items.quantity is 'Количество';
