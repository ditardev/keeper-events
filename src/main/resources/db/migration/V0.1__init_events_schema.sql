create schema if not exists event;

SET search_path TO event;

DROP TABLE IF EXISTS birthdays;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id   SERIAL PRIMARY KEY,
    uuid uuid not null unique
);

CREATE TABLE IF NOT EXISTS events
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER      not null,
    name   VARCHAR(100) not null,
    notify      boolean      NOT NULL DEFAULT FALSE,
    date        date         not null,
    type        VARCHAR(20)  NOT NULL DEFAULT 'ONCE',
    description text,
    UNIQUE (name, date)
);

ALTER TABLE events
    ADD FOREIGN KEY (user_id) REFERENCES users (id);