--==============================================================
-- ST4Example DB creation script for Apache Derby
--==============================================================
-- first add the apache nature to your project
-- second run Derby DBMS
-- third run this script with the help of ij utility
--==============================================================

-- this command connects ij to database st4db, if it doesn't exist DBMS creates one
CONNECT 'jdbc:derby://localhost:1527/st4db;create=true;user=vik;password=vik';

-- these commands remove all tables from the database
-- it implies an error if tables not exist in DB, just ignore it
DROP TABLE users;
DROP TABLE fullInformation;
DROP TABLE statuses;
DROP TABLE roles;
DROP TABLE rates_services;
DROP TABLE services;
DROP TABLE rates;


----------------------------------------------------------------
-- ROLES
-- users roles
----------------------------------------------------------------
CREATE TABLE services(

-- id has the INTEGER type (other name is INT), it is the primary key
	id INTEGER NOT NULL PRIMARY KEY,

-- name has the VARCHAR type - a string with a variable length
-- names values should not be repeated (UNIQUE) 	
	name VARCHAR(10) NOT NULL UNIQUE
);

-- this two commands insert data into roles table
----------------------------------------------------------------
-- ATTENTION!!!
-- we use ENUM as the Role entity, so the numeration must started 
-- from 0 with the step equaled to 1
----------------------------------------------------------------
INSERT INTO services VALUES(0, 'telephone');
INSERT INTO services VALUES(1, 'internet');
INSERT INTO services VALUES(2, 'cable_TV');
INSERT INTO services VALUES(3, 'IP-TV');
----------------------------------------------------------------
-- USERS
----------------------------------------------------------------
CREATE TABLE rates(

-- id has the INTEGER type (other name is INT), it is the primary key
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,

-- name has the VARCHAR type - a string with a variable length
-- names values should not be repeated (UNIQUE) 	
	name VARCHAR(10) NOT NULL UNIQUE,
	
	price INTEGER NOT NULL 
);

INSERT INTO rates VALUES(default, 'standart',80);
INSERT INTO rates VALUES(default, 'comfort',100);
INSERT INTO rates VALUES(default,'lux',150);
INSERT INTO rates VALUES(default,'NO_RATE',0);

CREATE TABLE rates_services(
	rates_id INTEGER NOT NULL REFERENCES rates(id),
	services_id INTEGER NOT NULL REFERENCES services(id)
);

INSERT INTO rates_services VALUES(1,0);
INSERT INTO rates_services VALUES(1,1);
INSERT INTO rates_services VALUES(2,0);
INSERT INTO rates_services VALUES(2,1);
INSERT INTO rates_services VALUES(2,3);
INSERT INTO rates_services VALUES(3,0);
INSERT INTO rates_services VALUES(3,1);
INSERT INTO rates_services VALUES(3,2);
INSERT INTO rates_services VALUES(3,3);

CREATE TABLE roles(

-- id has the INTEGER type (other name is INT), it is the primary key
	id INTEGER NOT NULL PRIMARY KEY,

-- name has the VARCHAR type - a string with a variable length
-- names values should not be repeated (UNIQUE) 	
	name VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO roles VALUES(0,'admin');
INSERT INTO roles VALUES(1,'subscriber');

CREATE TABLE fullInformation(

id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
first_name VARCHAR(20) NOT NULL,
last_name VARCHAR(20) NOT NULL,
telephone VARCHAR(15) NOT NULL UNIQUE,
address VARCHAR(40),
email VARCHAR(20) 

);

INSERT INTO fullInformation VALUES(default,'ivan','ivanov','0577162961','Sumskaya 12 apr.29','ivan.ivn@mail.ru');
INSERT INTO fullInformation VALUES(default,'petr','petrov','0932021139','pr.Gagarina 42 apr.16a','petr.ptr@google.com');

CREATE TABLE statuses(
    id INTEGER NOT NULL PRIMARY KEY,

-- name has the VARCHAR type - a string with a variable length
-- names values should not be repeated (UNIQUE) 	
	name VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO statuses VALUES(0,'blocked');
INSERT INTO statuses VALUES(1,'unblocked');

CREATE TABLE users(

-- 'generated always AS identity' means id is autoincrement field 
-- (from 1 up to Integer.MAX_VALUE with the step 1)
	id INTEGER NOT NULL generated always AS identity PRIMARY KEY,
	
-- 'UNIQUE' means logins values should not be repeated in login column of table	
	login VARCHAR(10) NOT NULL UNIQUE,
	
-- not null string columns	
	password VARCHAR(10) NOT NULL,
	fullInformation_id INTEGER NOT NULL REFERENCES fullInformation(id),
	
-- this declaration contains the foreign key constraint	
-- role_id in users table is associated with id in roles table
-- role_id of user = id of role
	personal_count INTEGER,
    statuses_id INTEGER NOT NULL REFERENCES statuses(id),
    rates_id INTEGER NOT NULL REFERENCES rates(id),
    roles_id INTEGER NOT NULL REFERENCES roles(id)
-- removing a row with some ID from roles table implies removing 
-- all rows from users table for which ROLES_ID=ID
-- default value is ON DELETE RESTRICT, it means you cannot remove
-- row with some ID from the roles table if there are rows in 
-- users table with ROLES_ID=ID
		--ON DELETE CASCADE 

-- the same as previous but updating is used insted deleting
		--ON UPDATE RESTRICT
);

-- id = 1
INSERT INTO users VALUES(DEFAULT, 'ivanAdm', 'admn', 1, 0, 1,4,0);
-- id = 2
INSERT INTO users VALUES(DEFAULT, 'petr12', '1346', 2, 70,1,1,1);
-- id = 3

----------------------------------------------------------------
-- STATUSES
-- statuses for orders
----------------------------------------------------------------

	
----------------------------------------------------------------
-- test database:
----------------------------------------------------------------
SELECT * FROM rates;
SELECT * FROM services;
SELECT * FROM rates_services;
SELECT * FROM roles;
SELECT * FROM statuses;
SELECT * FROM fullInformation;
SELECT * FROM users;

DISCONNECT;