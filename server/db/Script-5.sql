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

alter table document_management.version
add constraint fk_document_id_version foreign key (document_id) references document(id);

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
