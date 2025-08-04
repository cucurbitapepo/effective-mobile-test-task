--liquibase formatted sql
--changeset cucurbita:db localFilePath:01.000.00/cards.sql
CREATE TABLE cards
(
    card_id             UUID                            NOT NULL,
    card_number         bytea                           NOT NULL,
    user_id             uuid                            NOT NULL,
    expiration_date     DATE                            NOT NULL,
    card_status         VARCHAR(30)                     NOT NULL,
    balance             NUMERIC(15, 2)                  NOT NULL,
    CONSTRAINT pk_cards PRIMARY KEY (card_id),
    CONSTRAINT ch_card_status CHECK ( card_status in ('ACTIVE', 'BLOCKED', 'EXPIRED')),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);