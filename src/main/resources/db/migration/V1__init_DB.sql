create database it_market;
use it_market;
create table companies (
company_id int not null auto_increment,
company_name varchar(100),
rating varchar(50) not null, 
PRIMARY KEY (company_id),
check (rating in ('low', 'middle', 'hight'))
);
CREATE TABLE developers (
    developer_id int not null AUTO_INCREMENT,
    lastName varchar(50) not null,
    firstName varchar(50),
    age int not null,
    company_id int not null,
    salary int not null,
    PRIMARY KEY (developer_id),
    FOREIGN KEY (company_id) REFERENCES companies(company_id)
);
create table skills (
skill_id int not null auto_increment,
language varchar(50),
level varchar(50) not null,
primary key (skill_id),
check (level in ('junior', 'middle', 'senior'))
); 
create table developers_skills (
developer_id int not null,
skill_id int not null,
primary key (developer_id, skill_id),
foreign key (developer_id) references developers (developer_id),
foreign key (skill_id) references skills (skill_id)
);
create table customers (
customer_id int not null auto_increment,
customer_name varchar(100),
reputation varchar(50) not null check (reputation in ('insolvent', 'trustworthy', 'respectable')),
primary key (customer_id)
);
create table projects (
project_id int not null auto_increment,
project_name varchar(100) not null,
company_id int not null,
customer_id int not null,
cost int not null,
start_date date not null,
primary key (project_id),
foreign key (company_id) references companies (company_id),
foreign key (customer_id) references customers (customer_id)
);
create table projects_developers (
project_id int not null,
developer_id int not null,
primary key (project_id, developer_id),
foreign key (project_id ) references projects (project_id),
foreign key (developer_id) references developers (developer_id)
);