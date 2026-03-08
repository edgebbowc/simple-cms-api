-- TODO schema
-- 회원 테이블
create table users
(
    id       bigint primary key not null auto_increment,
    username varchar(10)        not null unique,
    password varchar(100)       not null,
    role     varchar(10)        not null  -- 'ADMIN' or 'USER'
);

-- 콘텐츠 테이블
create table contents
(
    id                 bigint primary key      not null auto_increment,
    title              varchar(100)            not null,
    description        text,
    view_count         bigint       default 0  not null,
    created_date       timestamp default now() not null,
    created_by         varchar(10)             not null,
    last_modified_date timestamp,
    last_modified_by   varchar(10)
);

-- 리프레시 토큰 테이블
create table refresh_tokens
(
    id         bigint primary key      not null auto_increment,
    username   varchar(10)             not null unique,
    token      varchar(512)            not null,
    expires_at timestamp               not null
);
