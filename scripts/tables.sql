
drop table Purchases;
drop table Supplies;
drop table Suppliers;
drop table Products;
drop table Discounts;
drop table Customers;
drop table Employees;
drop table Logs;

create table Logs
	(log# number(6) primary key,
	user_name varchar2(15),
	operation varchar2(15),
	op_time date,
	table_name varchar2(15),
	tuple_pkey varchar2(10));

create table Employees
	(eid char(3) primary key,
	name varchar2(15),
	telephone# char(12),
	email varchar2(20));

create table Customers
	(cid char(4) primary key,
	name varchar2(15),
	telephone# char(12),
	visits_made number(4) check (visits_made >= 1),
	last_visit_date date);

create table Discounts
	(discnt_category int primary key check (discnt_category >=1 AND discnt_category <=4),
	discnt_rate number(3,2));

create table Products
	(pid char(4) primary key,
	name varchar2(15),
	qoh number(5),
	qoh_threshold number(4),
	original_price number(6,2),
	discnt_category int references Discounts(discnt_category));

create table Suppliers
	(sid char(4) primary key,
	name varchar2(15),
	city varchar2(20),
	telephone# char(12),
	email varchar2(20));

create table Supplies
	(sup# number(6) primary key,
	pid char(4) references Products(pid),
	sid char(4) references Suppliers(sid),
	sdate date,
	quantity number(5));

create table Purchases
	(pur# number(6) primary key,
	eid char(3) references Employees(eid),
	pid char(4) references Products(pid),
	cid char(4) references Customers(cid),
	qty number(5),
	ptime date,
	total_price number(7,2));
