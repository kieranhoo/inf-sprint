drop database document_management;

CREATE DATABASE IF NOT exists document_management;

CREATE TABLE IF NOT EXISTS document_management.document(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	name varchar(30),
	description varchar(300),
	create_time Date,
	is_deleted boolean,
	date_deleted Date
);

create table if not exists document_management.version(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	document_id INTEGER,
	url varchar(300),
	name varchar(30),
	note varchar(200),
	current_version boolean,
	update_time Date	
);

CREATE TABLE IF NOT EXISTS document_management.users (
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	user_name VARCHAR(30) NOT null UNIQUE,
	email VARCHAR(100) NOT null UNIQUE,
	pass VARCHAR(255) NOT NULL,
	is_deleted BOOLEAN NOT NULL default false,
	date_deleted DATE
);

CREATE TABLE IF NOT EXISTS document_management.roles (
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	role VARCHAR(20) not null
);

CREATE table IF NOT EXISTS document_management.user_role (
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	user_id INTEGER,
	role_id INTEGER
);

CREATE TABLE IF NOT EXISTS document_management.token(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	token VARCHAR(255) not null UNIQUE,
	token_type_id INTEGER not null ,
	revoked boolean,
	expired boolean,
	user_id INTEGER not null,
    token_category_id INTEGER not null
);
CREATE TABLE IF NOT EXISTS document_management.token_type(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	token_type_name VARCHAR(100) not null UNIQUE
);
CREATE TABLE IF NOT EXISTS document_management.token_category(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	token_category_name VARCHAR(100) not null UNIQUE
);

alter table document_management.version
add constraint fk_document_id_version foreign key (document_id) references document(id);

ALTER TABLE document_management.user_role
ADD CONSTRAINT fk_user_id_user_role FOREIGN KEY (user_id) REFERENCES users(id),
ADD CONSTRAINT fk_role_id_user_role FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE document_management.token
ADD CONSTRAINT fk_token_type_id FOREIGN KEY (token_type_id) REFERENCES token_type(id),
ADD CONSTRAINT fk_token_user_id FOREIGN KEY (user_id) REFERENCES users(id),
ADD CONSTRAINT fk_token_category_id FOREIGN KEY (token_category_id) REFERENCES token_category(id);

INSERT INTO document_management.roles (id,role)
VALUES
(1,'ADMIN'),
(2,'EMPLOYEE');

INSERT INTO document_management.document (name,description,create_time,is_deleted,date_deleted) VALUES
	 ('Anh dep trai','File nay de test','2023-11-06',0,NULL),
	 ('document 2','New Doc','2023-11-06',0,NULL),
	 ('document 3','New Doc','2023-11-06',0,NULL),
	 ('document 4','New Doc','2023-11-06',0,NULL),
	 ('document 5','New Doc','2023-11-06',0,NULL);
	
INSERT INTO document_management.version (document_id,url,name,note,current_version,update_time) VALUES
	 (1,'21232112321eaaafdf','1.0.0',NULL,0,'2023-11-06'),
	 (2,'21232112321eaaafdf','1.0.0',NULL,1,'2023-11-06'),
	 (3,'21232112321eaaafdf','1.0.0',NULL,1,'2023-11-06'),
	 (4,'21232112321eaaafdf','1.0.0',NULL,1,'2023-11-06'),
	 (5,'21232112321eaaafdf','1.0.0',NULL,1,'2023-11-06'),
	 (1,'Link tam bay tam ba','1.0.1',NULL,1,'2023-11-06');
	
INSERT INTO document_management.token_type (id,token_type_name)
VALUES
(1,'BEARER');

INSERT INTO document_management.token_category (id,token_category_name)
VALUES
(1,'ACCESS'),
(2,'REFRESH');

