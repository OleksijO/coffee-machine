USE `coffee_machine`;

INSERT INTO account (amount) VALUES (0), (9999999999), (999999);

INSERT INTO abstract_user (id, email, password, full_name) VALUES
  (1, 'oleksij.onysymchuk@gmail.com', '630d82b1215e611e7aca66237834cd19', 'Олексій Онисимчук'),
  (2, 'user@test.com', '630d82b1215e611e7aca66237834cd19', 'Тестовий користувач'),
  (3, 'admin@test.com', '630d82b1215e611e7aca66237834cd19', 'Тестовий адміністратор');

INSERT INTO users (user_id, account_id) VALUES
  (1, 2),
  (2, 3);

INSERT INTO admins (admin_id, enabled) VALUES
  (1, FALSE),
  (3, TRUE );

INSERT INTO abstract_goods (id, name, price, quantity) VALUES
  (1,'Вода',100, 20),
  (2,'Боржоми',500, 20),
  (3,'Чай без сахара',500, 10),
  (4,'Чай с сахаром',600, 20),
  (5,'Лимон',200, 20),
  (6,'Эспрессо',700, 50),
  (7,'Дополнительный сахар',100, 300),
  (8,'Молоко',200, 150),
  (9,'Сливки',300, 150),
  (10,'Американо',800, 150),
  (11,'Мокачино',1000, 50),
  (12,'Латте',1200, 100),
  (13,'Корица',150, 75);


INSERT INTO drink(id) VALUES
  (1),
  (2),
  (3),
  (4),
  (6),
  (10),
  (11),
  (12);

INSERT INTO addon(id) VALUES
  (5),
  (7),
  (8),
  (9),
  (13);

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




