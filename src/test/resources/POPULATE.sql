USE `test_coffee_machine`;

INSERT INTO account (amount) VALUES (0), (9999999999), (999999);

INSERT INTO users (id, email, password, full_name, account_id, is_admin) VALUES
  (1, 'oleksij.onysymchuk@gmail.com', '495286b908f344a71f0895d3258f5e4a', 'Олексій Онисимчук', 2, TRUE),
  (2, 'user@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий користувач', 3, FALSE),
  (3, 'admin@test.com', '495286b908f344a71f0895d3258f5e4a', 'Тестовий адміністратор', NULL, TRUE);

INSERT INTO item (id, name, price, quantity, type) VALUES
  (1, 'Вода', 100, 20, 'DRINK'),
  (2, 'Боржоми', 500, 20, 'DRINK'),
  (3, 'Чай без сахара', 500, 10, 'DRINK'),
  (4, 'Чай с сахаром', 600, 20, 'DRINK'),
  (5, 'Лимон', 200, 20, 'ADDON'),
  (6, 'Эспрессо', 700, 50, 'DRINK'),
  (7, 'Дополнительный сахар', 100, 300, 'ADDON'),
  (8, 'Молоко', 200, 150, 'ADDON'),
  (9, 'Сливки', 300, 150, 'ADDON'),
  (10, 'Американо', 800, 150, 'DRINK'),
  (11, 'Мокачино', 1000, 50, 'DRINK'),
  (12, 'Латте', 1200, 100, 'DRINK'),
  (13, 'Корица', 150, 75, 'ADDON');


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

INSERT INTO history_record (id, user_id, date_time, order_description, amount) VALUES

  (1, 1, '2016-06-18 15:05:00', 'Some order description 1', 1000),
  (2, 1, '2016-06-18 15:15:00', 'Some order description 2', 2000),
  (3, 1, '2016-06-18 15:25:00', 'Some order description 3', 3000),
  (4, 2, '2016-06-18 15:35:00', 'Some order description 4', 4000),
  (5, 2, '2016-06-18 15:45:00', 'Some order description 5', 5000),
  (6, 2, '2016-06-18 15:55:00', 'Some order description 6', 6000);


