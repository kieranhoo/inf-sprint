drop database document_management;

CREATE DATABASE IF NOT exists document_management;

CREATE TABLE IF NOT EXISTS document_management.document(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	name varchar(30),
	create_time time,
	is_delete boolean,
	date_deleted time
);

create table if not exists document_management.version(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	document_id INTEGER,
	url varchar(300),
	name varchar(30),
	current_version boolean,
	update_time time	
);

alter table document_management.version
add constraint fk_document_id_version foreign key (document_id) references document(id);
