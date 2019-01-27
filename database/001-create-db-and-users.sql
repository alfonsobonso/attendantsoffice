create database event;

use event;

CREATE TABLE user (
	user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	home_phone VARCHAR(15),
	mobile_phone VARCHAR(15),
	email VARCHAR(100) NOT NULL UNIQUE,
	user_status VARCHAR(12) NOT NULL,
	congregation_id INT NOT NULL,
	position VARCHAR(12) NOT NULL,
	role VARCHAR(12)  NOT NULL,
	password VARCHAR(100),
	last_signin_date_time TIMESTAMP,
	created_by_user_id INT NOT NULL,
	created_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_by_user_id INT NOT NULL,
	updated_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE authentication_token (
	authentication_token_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL,
	token CHAR(36) NOT NULL,
	used BOOLEAN NOT NULL,
	created_by_user_id INT NOT NULL,
	created_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_by_user_id INT,
	updated_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE congregation (
    congregation_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_by_user_id INT NOT NULL,
    created_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event (
    event_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    event_status VARCHAR(12) NOT NULL,
    current BOOLEAN NOT NULL,
    created_by_user_id INT,
    created_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by_user_id INT,
    updated_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE event_team (
    event_team_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    event_id INT UNSIGNED NOT NULL,
    name VARCHAR(100) NOT NULL,
    name_with_captain VARCHAR(500) NOT NULL,
    parent_event_team_id INT UNSIGNED,
    created_by_user_id INT NOT NULL,
    created_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by_user_id INT,
    updated_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY fk_event(event_id) REFERENCES event(event_id),
    FOREIGN KEY fk_parent_event_team(parent_event_team_id) REFERENCES event_team(event_team_id),
    CONSTRAINT uq_name UNIQUE(event_id, name)
);

-- assignment types, linked to the user team assignment
-- we use codes rather than IDs because there is logic in the application around
-- who can be assigned, and the impact of that
CREATE TABLE assignment (
	assignment_code CHAR(5) PRIMARY KEY,
	name VARCHAR(100) NOT NULL
);

INSERT INTO assignment(assignment_code, name)
VALUES 
	('AOVRR', 'Attendants Overseer'),
	('AAOVR', 'Asst Overseer'),
	('DCAPN', 'Division Captain'),
	('ADCPN', 'Asst Division Captain'),
	('TCAPN', 'Team Captain'),
	('ATCPN', 'Asst Team Captain'),
	('ATNDT', 'Attendant'),
	('CRAST', 'Care Assistant'),
	('ROVRR', 'Registration Overseer'),
	('AROVR', 'Asst Registration Overseer'),
	('OFSPT', 'Office Support'),
	('IICTR', 'IBSA Incident Controller'),
	('POVRR', 'PMR Overseer'),
	('APOVR', 'Asst PMR Overseer');
	
-- assign a user to an event team
-- we denormalise the event and event team for easy searching	
CREATE TABLE event_team_user_assignment (
    event_team_user_assignment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNSIGNED NOT NULL,
    event_id INT UNSIGNED NOT NULL,
    event_team_id INT UNSIGNED NOT NULL,
    assignment_code CHAR(5) NOT NULL,
    assignment_status_code CHAR(3) NOT NULL,
    created_by_user_id INT NOT NULL,
    created_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by_user_id INT,
    updated_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY fk_user_id(user_id) REFERENCES user(user_id),
    FOREIGN KEY fk_event_id(event_id) REFERENCES event(event_id),
    FOREIGN KEY fk_event_team_id(event_team_id) REFERENCES event_team(event_team_id),
    FOREIGN KEY fk_assignment_code(assignment_code) REFERENCES assignment(assignment_code),
    CONSTRAINT uq_user_event_team UNIQUE(user_id, event_team_id)
);
