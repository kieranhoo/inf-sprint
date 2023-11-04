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
