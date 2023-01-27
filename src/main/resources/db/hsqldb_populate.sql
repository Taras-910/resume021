DELETE FROM rate;
DELETE FROM freshen_goal;
DELETE FROM freshen;
DELETE FROM vote;
DELETE FROM user_roles;
DELETE FROM resume;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password, registered)
VALUES ('Admin', 'admin@gmail.com', '{noop}admin', '2020-01-30 8:00:00'),
       ('User', 'user@yandex.ru', '{noop}password', '2020-01-30 8:00:00');

INSERT INTO user_roles (role, user_id)
VALUES ('ADMIN', 100000),
       ('USER', 100001);

INSERT INTO freshen (recorded_date, language, level, workplace, user_id)
VALUES ('2020-10-25 12:00:00', 'java', 'middle', 'киев', 100000),
       ('2020-10-25 13:00:00', 'php', 'middle', 'днепр', 100001);

INSERT INTO freshen_goal (goal, freshen_id)
VALUES ('UPGRADE', 100002),
       ('FILTER', 100003);

INSERT INTO resume (title, name, age, address, salary, work_before, url, skills, release_date, freshen_id)
VALUES ('Middle Game Developer', 'Василь Васильевич', '21', 'Киев', 3000, 'КСПО, 10 лет и 2 месяца',
        'https://grc.ua/resume/40006938?query=java', 'Spring, SQL, REST, PHP', '2021-10-20', 100002),
       ('Middle Java-разработчик', 'Виктор Михайлович', '22', 'Днепр', 2000, 'Днепросталь, 4 года и 5 месяцев',
        'https://grc.ua/resume/40006938?query=java',
        'Понимание JVM. Умение отлаживать и профилировать java-приложения', '2021-10-20', 100003);

INSERT INTO vote (local_date, resume_id, user_id)
VALUES ('2020-10-25', 100004, 100000),
       ('2020-10-25', 100005, 100001);

INSERT INTO rate (name, value_rate, date_rate)
VALUES ('USDUSD', 1.0, '2020-10-25'),
       ('USDUAH', 36.53, '2020-10-25'),
       ('USDPLN', 4.8544, '2020-10-25'),
       ('USDKZT', 469.5, '2020-10-25'),
       ('USDGBP', 0.87148, '2020-10-25'),
       ('USDEUR', 1.00711, '2020-10-25'),
       ('USDCZK', 24.7275, '2020-10-25'),
       ('USDCAD', 1.35791, '2020-10-25'),
       ('USDBGN', 1.9701, '2020-10-25'),
       ('USDBYR', 2.52, '2020-10-25');
