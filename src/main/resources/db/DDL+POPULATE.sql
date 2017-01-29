DROP SCHEMA IF EXISTS coffee_machine;

CREATE DATABASE IF NOT EXISTS coffee_machine;
USE coffee_machine;

DROP TABLE IF EXISTS product;

CREATE TABLE product (
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
  role       VARCHAR(16)        NOT NULL,
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
  CONSTRAINT drink_fk1 FOREIGN KEY (drink_id) REFERENCES product (id)
    ON DELETE CASCADE,
  CONSTRAINT addon_fk1 FOREIGN KEY (addon_id) REFERENCES product (id)
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
  orders_id INT(11) NOT NULL,
  drink_id  INT(11) NOT NULL,
  quantity  INT(11) NOT NULL,
  PRIMARY KEY orders_drink_pk (id),
  CONSTRAINT drink_id_fk FOREIGN KEY (drink_id) REFERENCES product (id),
  CONSTRAINT orders_fk FOREIGN KEY (orders_id) REFERENCES orders (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE orders_addon (
  orders_drink_id INT(11) NOT NULL,
  addon_id        INT(11) NOT NULL,
  quantity        INT(11) NOT NULL,
  CONSTRAINT addon_id_fk FOREIGN KEY (addon_id) REFERENCES product (id),
  PRIMARY KEY orders_addon_pk (orders_drink_id, addon_id),
  CONSTRAINT orders_addon_orders_drink_id_fk FOREIGN KEY (orders_drink_id) REFERENCES orders_drink (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO account (amount) VALUES (0), (0), (10000), (10000), (100000);

INSERT INTO users (id, email, password, full_name, account_id, role) VALUES
  (1, 'admin@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий адміністратор', NULL, 'ADMIN'),
  (2, 'user@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий користувач', 2, 'USER'),
  (3, 'user1@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий користувач 2', 3, 'USER'),
  (4, 'user2@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий користувач 3', 4, 'USER'),
  (5, 'user3@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий користувач 4', 5, 'USER');


INSERT INTO product (id, name, price, quantity, type) VALUES
  (1, 'Вода', 100, 10, 'DRINK'),
  (2, 'Минеральная вода', 500, 10, 'DRINK'),
  (3, 'Чай без сахара', 500, 10, 'DRINK'),
  (4, 'Чай с сахаром', 600, 10, 'DRINK'),
  (5, 'Лимон', 200, 20, 'ADDON'),
  (6, 'Эспрессо', 700, 10, 'DRINK'),
  (7, 'Дополнительный сахар', 100, 20, 'ADDON'),
  (8, 'Молоко', 200, 20, 'ADDON'),
  (9, 'Сливки', 300, 20, 'ADDON'),
  (10, 'Американо', 800, 10, 'DRINK'),
  (11, 'Мокачино', 1000, 10, 'DRINK'),
  (12, 'Латте', 1200, 10, 'DRINK'),
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

INSERT INTO orders VALUES
  (1, 5, '2017-01-29 13:52:10', 100),
  (2, 5, '2017-01-29 13:52:13', 500),
  (3, 5, '2017-01-29 13:52:18', 700),
  (4, 5, '2017-01-29 13:52:31', 1300),
  (5, 5, '2017-01-29 13:52:38', 1000),
  (6, 5, '2017-01-29 13:52:43', 1400),
  (7, 5, '2017-01-29 13:52:52', 1700),
  (8, 5, '2017-01-29 13:53:07', 600),
  (9, 5, '2017-01-29 13:53:11', 100),
  (10, 5, '2017-01-29 13:53:16', 900),
  (11, 5, '2017-01-29 13:53:23', 1600),
  (12, 5, '2017-01-29 13:53:32', 900),
  (13, 5, '2017-01-29 13:53:50', 4200),
  (14, 5, '2017-01-29 13:54:00', 1500),
  (15, 5, '2017-01-29 13:54:03', 800),
  (16, 5, '2017-01-29 13:54:06', 1200),
  (17, 5, '2017-01-29 13:54:09', 600),
  (18, 5, '2017-01-29 13:54:14', 1400),
  (19, 5, '2017-01-29 13:54:17', 1600),
  (20, 5, '2017-01-29 13:54:20', 2000),
  (21, 5, '2017-01-29 13:54:23', 1000),
  (22, 5, '2017-01-29 13:54:26', 600),
  (23, 5, '2017-01-29 13:54:43', 1000),
  (24, 5, '2017-01-29 13:55:01', 3050);

INSERT INTO orders_drink VALUES
  (1, 1, 1, 1),
  (2, 2, 2, 1),
  (3, 3, 3, 1),
  (4, 4, 10, 1),
  (5, 5, 10, 1),
  (6, 6, 12, 1),
  (7, 7, 1, 1),
  (8, 7, 2, 1),
  (9, 7, 3, 1),
  (10, 7, 4, 1),
  (11, 8, 4, 1),
  (12, 9, 1, 1),
  (13, 10, 10, 1),
  (14, 11, 11, 1),
  (15, 12, 4, 1),
  (16, 13, 10, 3),
  (17, 14, 1, 1),
  (18, 14, 2, 1),
  (19, 14, 3, 1),
  (20, 15, 10, 1),
  (21, 16, 12, 1),
  (22, 17, 4, 1),
  (23, 18, 6, 2),
  (24, 19, 10, 2),
  (25, 20, 11, 2),
  (26, 21, 11, 1),
  (27, 22, 4, 1),
  (28, 23, 11, 1),
  (29, 24, 4, 1),
  (30, 24, 6, 1),
  (31, 24, 10, 1);

INSERT INTO orders_addon VALUES
  (3, 5, 1),
  (4, 7, 2),
  (4, 9, 1),
  (5, 8, 1),
  (6, 7, 2),
  (13, 7, 1),
  (14, 9, 2),
  (15, 5, 1),
  (15, 7, 1),
  (16, 9, 2),
  (19, 5, 2),
  (29, 5, 1),
  (29, 7, 2),
  (30, 7, 2),
  (31, 7, 2),
  (31, 13, 1);
