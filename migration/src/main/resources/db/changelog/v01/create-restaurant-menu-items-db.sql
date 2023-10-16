create sequence if not exists res_menu_items_seq;

create table if not exists restaurant_menu_items
(
    id            bigint         not null default nextval('res_menu_items_seq'),
    restaurant_id bigint         not null,
    name          varchar(255)   not null,
    price         decimal(10, 2) not null,
    image         bytea,
    description   varchar(1000),
    constraint res_menu_items_pk primary key (id),
    constraint res_menu_items_fk foreign key (restaurant_id) references restaurants (id)
);

comment on table restaurant_menu_items is 'Таблица блюд меню со ссылкой на ресторан';
comment on column restaurant_menu_items.id is 'Идентификатор';
comment on column restaurant_menu_items.restaurant_id is 'Ссылка на конкретный ресторан';
comment on column restaurant_menu_items.name is 'Наименование блюда';
comment on column restaurant_menu_items.price is 'Цена блюда';
comment on column restaurant_menu_items.image is 'Изображение блюда';
comment on column restaurant_menu_items.description is 'Описание состав блюда';
