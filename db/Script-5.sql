drop database document_management;

CREATE DATABASE IF NOT exists document_management;

CREATE TABLE IF NOT EXISTS document_management.document(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	name varchar(30),
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

-- Add dummy values to the 'document' table with DATETIME
INSERT INTO document_management.document (name, create_time, is_deleted, date_deleted)
VALUES
    ('Document 1', '2023-11-04 00:00:00', false, null),
    ('Document 2', '2023-11-05 00:00:00', false, null),
    ('Document 3', '2023-11-06 00:00:00', false, null);

-- Add dummy values to the 'version' table with DATETIME
INSERT INTO document_management.version (document_id, url, name, note, current_version, update_time)
VALUES
    (1, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-04 00:00:00'),
    (1, 'https://example.com/version/2', 'Version 2', 'Updated content', false, '2023-11-05 00:00:00'),
    (2, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-05 00:00:00'),
    (3, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-06 00:00:00');

