drop database document_management;

CREATE DATABASE IF NOT exists document_management;

CREATE TABLE IF NOT EXISTS document_management.document(
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	department_id INTEGER,
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

CREATE TABLE if not exists document_management.department (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(300)
);

CREATE TABLE if not exists document_management.user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL,
    department_id INT
);

alter table document_management.version
add constraint fk_document_id_version foreign key (document_id) references document(id);

alter table document_management.document
add constraint fk_dep_id_document foreign key (department_id) references department(id);

alter table document_management.user
add constraint fk_user_id_document foreign key (department_id) references department(id);

-- Insert 10 dummy records into the 'department' table
INSERT INTO document_management.department (name, description)
VALUES
    ('Department 1', 'Description 1'),
    ('Department 2', 'Description 2'),
    ('Department 3', 'Description 3');

-- Insert 10 dummy records into the 'document' table
INSERT INTO document_management.document (department_id, name, description, create_time, is_deleted, date_deleted)
VALUES
    (1, 'Document 1', 'Description 1', '2023-11-01', false, null),
    (2, 'Document 2', 'Description 2', '2023-11-02', false, null),
    (3, 'Document 3', 'Description 3', '2023-11-03', false, null),
    (1, 'Document 4', 'Description 4', '2023-11-04', false, null),
    (2, 'Document 5', 'Description 5', '2023-11-05', false, null),
    (3, 'Document 6', 'Description 6', '2023-11-06', false, null),
    (1, 'Document 7', 'Description 7', '2023-11-07', false, null),
    (2, 'Document 8', 'Description 8', '2023-11-08', false, null),
    (2, 'Document 9', 'Description 9', '2023-11-09', false, null),
    (1, 'Document 10', 'Description 10', '2023-11-10', false, null);

-- Insert 10 dummy records into the 'version' table
INSERT INTO document_management.version (document_id, url, name, note, current_version, update_time)
VALUES
    (1, 'https://example.com/version/1', 'Version 1', 'Initial version', false, '2023-11-01'),
    (1, 'https://example.com/version/2', 'Version 2', 'Updated content', true, '2023-11-02'),
    (2, 'https://example.com/version/1', 'Version 1', 'Initial version', false, '2023-11-02'),
    (2, 'https://example.com/version/2', 'Version 2', 'Updated content 1', false, '2023-11-02'),
    (2, 'https://example.com/version/3', 'Version 3', 'Updated content 2', true, '2023-11-02'),
    (3, 'https://example.com/version/1', 'Version 1', 'Initial version', false, '2023-11-03'),
    (3, 'https://example.com/version/2', 'Version 2', 'Updated content ALA', true, '2023-11-03'),
    (4, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-04'),
    (5, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-05'),
    (6, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-06'),
    (7, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-07'),
    (8, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-08'),
    (9, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-09'),
    (10, 'https://example.com/version/1', 'Version 1', 'Initial version', true, '2023-11-10');

INSERT INTO user (name, email, username, group_id)
   VALUES
       ('User 1', 'user1@example.com', 'user1', 1),
       ('User 2', 'user2@example.com', 'user2', 1),
       ('User 3', 'user3@example.com', 'user3', 2),
       ('User 4', 'user4@example.com', 'user4', 2),
       ('User 5', 'user5@example.com', 'user5', 3),
       ('User 6', 'user6@example.com', 'user6', 3),
       ('User 7', 'user7@example.com', 'user7', 4),
       ('User 8', 'user8@example.com', 'user8', 4),
       ('User 9', 'user9@example.com', 'user9', 5),
       ('User 10', 'user10@example.com', 'user10', 5),
       ('User 11', 'user11@example.com', 'user11', 1),
       ('User 12', 'user12@example.com', 'user12', 1),
       ('User 13', 'user13@example.com', 'user13', 2),
       ('User 14', 'user14@example.com', 'user14', 2),
       ('User 15', 'user15@example.com', 'user15', 3),
       ('User 16', 'user16@example.com', 'user16', 3),
       ('User 17', 'user17@example.com', 'user17', 4),
       ('User 18', 'user18@example.com', 'user18', 4),
       ('User 19', 'user19@example.com', 'user19', 5),
       ('User 20', 'user20@example.com', 'user20', 5);

