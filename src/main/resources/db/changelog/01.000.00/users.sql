--liquibase formatted sql
--changeset cucurbita:db localFilePath:01.000.00/users.sql
CREATE TABLE users
(
    user_id             UUID                            NOT NULL,
    first_and_last_name VARCHAR(50)                     NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);