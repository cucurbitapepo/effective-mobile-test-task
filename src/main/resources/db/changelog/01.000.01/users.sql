--liquibase formatted sql
--changeset cucurbita:db localFilePath:01.000.01/users.sql
ALTER TABLE users ADD username VARCHAR(50) NOT NULL UNIQUE;
ALTER TABLE users ADD password VARCHAR(100) NOT NULL;
ALTER TABLE users ADD role VARCHAR(20) NOT NULL;

ALTER TABLE users ADD CONSTRAINT ch_role CHECK ( role in ('ROLE_USER', 'ROLE_ADMIN'));
