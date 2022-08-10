insert into companies(company_name, rating) values
('Luxoft', 'high'),
('GeeksForLess Inc.', 'middle'),
('Beetroot', 'low');
insert into developers(lastName, firstName, age, company_id, salary) values
('Chimadurov', 'Ruslan', 56, 1, 1000),
('Stepanova', 'Tetyana', 34, 2, 1500),
('Havrilitse', 'Vadim', 32, 3, 2000),
('Petrenko', 'Vladimir', 28, 2, 1600),
('Omelyashko', 'Olexey', 30, 1, 1800),
('Zerko', 'Andrew', 26, 3, 1700),
('Yakovenko', 'Vyacheslav', 24, 1, 2500);
insert into skills (language, level) values
('Java', 'junior'), ('Java', 'middle'), ('Java', 'senior'),
('JS', 'junior'), ('JS', 'middle'), ('JS', 'senior'),
('C++', 'junior'), ('C++', 'middle'), ('C++', 'senior'),
('PHP', 'junior'), ('PHP', 'middle'), ('PHP', 'senior');
insert into  developers_skills (developer_id, skill_id) values
(1, 1),
(2, 1), (2, 4), (2, 10),
(3, 2), (3, 11),
(4, 2),
(5, 1), (5, 10),
(6, 2), (6, 8),
(7, 3), (7, 9), (7, 12);
insert into customers (customer_name,reputation) values
('IBM', 'trustworthy'),
('Apple', 'respectable'),
('Fasebook', 'insolvent'),
('Oracle', 'respectable');
insert into projects (project_name, company_id, customer_id, cost, start_date) values
('MetaUnivers', 1, 3, 15000, '2010-10-01'),
('Cristal Eye', 3, 2, 50000, '2015-06-01'),
('Super Technology', 2, 1, 40000, '2020-04-01'),
('Global DB', 1, 4, 25000, '2022-01-01'),
('Telecom Revolution', 3, 4, 25000, '2022-01-01');
insert into projects_developers (project_id, developer_id) values
(1, 1), (1,5),(1, 7),
(2, 3),
(3, 2), (3, 4),
(4, 1), (4, 7),
(5, 6);