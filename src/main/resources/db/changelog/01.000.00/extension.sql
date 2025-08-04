--liquibase formatted sql
--changeset cucurbita:db localFilePath:01.000.00/extension.sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;