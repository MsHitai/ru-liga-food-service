alter table orders
    add column time_stamp timestamp not null default now();