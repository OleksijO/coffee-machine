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

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id        INT(11)    NOT NULL AUTO_INCREMENT,
  user_id   INT(11)    NOT NULL,
  date_time TIMESTAMP  NOT NULL,
  amount    BIGINT(20) NOT NULL DEFAULT '0',
  PRIMARY KEY id_pk (id),
  KEY order_user_fk_idx (user_id),
  CONSTRAINT orders_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE orders_drink (
  id        INT(11) NOT NULL AUTO_INCREMENT,
  orders_id INT(11) NOT NULL ,
  drink_id  INT(11) NOT NULL,
  quantity  INT(11) NOT NULL,
  PRIMARY KEY orders_drink_pk (id),
  CONSTRAINT drink_id_fk FOREIGN KEY (drink_id) REFERENCES item (id),
  CONSTRAINT orders_fk FOREIGN KEY (orders_id) REFERENCES orders (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE orders_addon (
  orders_drink_id INT(11) NOT NULL,
  addon_id  INT(11) NOT NULL,
  quantity  INT(11) NOT NULL,
  CONSTRAINT addon_id_fk FOREIGN KEY (addon_id) REFERENCES item (id),
  PRIMARY KEY orders_addon_pk (orders_drink_id, addon_id),
  CONSTRAINT orders_addon_orders_drink_id_fk FOREIGN KEY (orders_drink_id) REFERENCES orders_drink (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO account (amount) VALUES (0), (0);

INSERT INTO users (id, email, password, full_name, account_id, is_admin) VALUES
  (1, 'admin@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий адміністратор', NULL, TRUE),
  (2, 'user@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий користувач', 2, FALSE);

INSERT INTO item (id, name, price, quantity, type) VALUES
  (1, 'Вода', 100, 10, 'DRINK'),
  (2, 'Боржоми', 500, 10, 'DRINK'),
  (3, 'Чай без сахара', 500, 10, 'DRINK'),
  (4, 'Чай с сахаром', 600, 10, 'DRINK'),
  (5, 'Лимон', 200, 20, 'ADDON'),
  (6, 'Эспрессо', 700, 10, 'DRINK'),
  (7, 'Дополнительный сахар', 100, 20, 'ADDON'),
  (8, 'Молоко', 200, 20, 'ADDON'),
  (9, 'Сливки', 300, 20, 'ADDON'),
  (10, 'Американо', 800, 10, 'DRINK'),
  (11, 'Мокачино', 1000, 10, 'DRINK'),
  (12, 'Латте', 1200, 100, 'DRINK'),
  (13, 'Корица', 150, 20, 'ADDON');

INSERT INTO drink_addons (drink_id, addon_id) VALUES
  (3, 5),
  (4, 5),
  (4, 7),
  (6, 7),
  (10, 7),
  (11, 7),
  (12, 7),
  (6, 8),
  (10, 8),
  (11, 8),
  (6, 9),
  (10, 9),
  (11, 9),
  (6, 13),
  (10, 13),
  (11, 13),
  (12, 13);
