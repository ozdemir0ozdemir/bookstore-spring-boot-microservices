create sequence order_event_id_seq start with 1 increment by 50;

create table order_events
(
    id         bigint not null default nextval('order_event_id_seq'),
    event_id   text   not null unique,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);