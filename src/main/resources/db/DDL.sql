DROP SCHEMA IF EXISTS coffee_machine;

CREATE DATABASE IF NOT EXISTS coffee_machine ;
USE coffee_machine;

DROP TABLE IF EXISTS abstract_goods;

CREATE TABLE abstract_goods (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  price bigint(20) NOT NULL DEFAULT '0',
  quantity int(11) DEFAULT '0',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS abstract_user;

CREATE TABLE abstract_user (
  id int(11) NOT NULL AUTO_INCREMENT,
  email varchar(60) NOT NULL,
  password varchar(32) NOT NULL,
  full_name varchar(80) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY email_UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS account;

CREATE TABLE account (
  id int(11) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS addon;

CREATE TABLE addon (
  id int(11) NOT NULL,
  UNIQUE KEY id_UNIQUE (id),
  KEY goods_fk_idx (id),
  CONSTRAINT goods_addon_fk FOREIGN KEY (id) REFERENCES abstract_goods (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS admins;

CREATE TABLE admins (
  admin_id int(11) NOT NULL,
  enabled bit(1) NOT NULL,
  UNIQUE KEY id_UNIQUE (admin_id),
  CONSTRAINT admin_fk FOREIGN KEY (admin_id) REFERENCES abstract_user (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS drink;

CREATE TABLE drink (
  id int(11) NOT NULL,
  UNIQUE KEY id_UNIQUE (id),
  KEY goods_fk_idx (id),
  CONSTRAINT goods_drink_fk FOREIGN KEY (id) REFERENCES abstract_goods (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS drink_addons;

CREATE TABLE drink_addons (
  drink_id int(11) NOT NULL,
  addon_id int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  user_id int(11) NOT NULL,
  account_id int(11) NOT NULL,
  UNIQUE KEY user_id_UNIQUE (user_id),
  KEY account_id_idx (account_id),
  KEY user_fk_idx (user_id),
  CONSTRAINT account_fk FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
  CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES abstract_user (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS history_record;

CREATE TABLE history_record (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  date_dime TIMESTAMP NOT NULL,
  order_description TEXT NOT NULL,
  amount bigint(20) NOT NULL DEFAULT '0',
  UNIQUE KEY id_UNIQUE (id),
  KEY user_fk1_idx (user_id),
  CONSTRAINT user_fk1 FOREIGN KEY (user_id) REFERENCES abstract_user (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
