create sequence if not exists category_id_seq start with 101 increment by 50;

create table if not exists categories
(
    id          bigint    not null default nextval('category_id_seq'),
    name        text      not null,
    slug        text      not null,
    primary key (id),
    constraint uk_categories_name unique (name),
    constraint uk_categories_slug unique (slug)
);


create sequence bookmark_id_seq start with 101 increment by 50;

create table bookmarks
(
    id          bigint    not null default nextval('bookmark_id_seq'),
    title       text      not null,
    url         text      not null,
    description text,
    primary key (id)
);