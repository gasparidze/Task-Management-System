--liquibase formatted sql

--changeset agasparyan:1
CREATE SCHEMA IF NOT EXISTS task_service;

--changeset agasparyan:2
CREATE TABLE IF NOT EXISTS task_service.user
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(128)       NOT NULL,
    name     VARCHAR(50)        NOT NULL
);

--changeset agasparyan:3
CREATE TABLE IF NOT EXISTS task_service.task
(
    id           BIGSERIAL PRIMARY KEY,
    header       VARCHAR(100) NOT NULL,
    description  VARCHAR(200) NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    priority     VARCHAR(20)  NOT NULL,
    created_date DATE         NOT NULL,
    author_id    BIGINT       NOT NULL REFERENCES task_service.user (id),
    performer_id BIGINT       NOT NULL REFERENCES task_service.user (id)
);

--changeset agasparyan:4
CREATE TABLE IF NOT EXISTS task_service.comment
(
    id           BIGSERIAL PRIMARY KEY,
    text         TEXT NOT NULL,
    created_date DATE NOT NULL,
    task_id      BIGINT REFERENCES task_service.task (id) ON DELETE CASCADE
);
