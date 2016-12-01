DROP SCHEMA IF EXISTS coffee_machine;

CREATE DATABASE IF NOT EXISTS coffee_machine ;
USE coffee_machine;

DROP TABLE IF EXISTS item;

CREATE TABLE item (
  id       INT(11)     NOT NULL AUTO_INCREMENT,
  name     VARCHAR(45) NOT NULL,
  price    BIGINT(20)  NOT NULL DEFAULT '0',
  quantity INT(11)              DEFAULT '0',
  type     VARCHAR(10) NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS abstract_user;

DROP TABLE IF EXISTS account;

CREATE TABLE account (
  id     INT(11)    NOT NULL AUTO_INCREMENT,
  amount BIGINT(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE users (
  id         INT(11)            NOT NULL AUTO_INCREMENT,
  email      VARCHAR(60) UNIQUE NOT NULL,
  password   VARCHAR(32)        NOT NULL,
  full_name  VARCHAR(80)        NOT NULL,
  account_id INT(11),
  is_admin   BIT(1)             NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT account_fk FOREIGN KEY (account_id) REFERENCES account (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS drink_addons;

CREATE TABLE drink_addons (
  drink_id INT(11) NOT NULL,
  addon_id INT(11) NOT NULL,
  KEY drink_fk1_idx (drink_id),
  CONSTRAINT drink_fk1 FOREIGN KEY (drink_id) REFERENCES item (id)
    ON DELETE CASCADE,
  CONSTRAINT addon_fk1 FOREIGN KEY (addon_id) REFERENCES item (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS history_record;

CREATE TABLE history_record (
  id                INT(11)    NOT NULL AUTO_INCREMENT,
  user_id           INT(11)    NOT NULL,
  date_time         TIMESTAMP  NOT NULL,
  order_description TEXT       NOT NULL,
  amount            BIGINT(20) NOT NULL DEFAULT '0',
  UNIQUE KEY id_UNIQUE (id),
  KEY user_fk1_idx (user_id),
  CONSTRAINT user_fk1 FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;