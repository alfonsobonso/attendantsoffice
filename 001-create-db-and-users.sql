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

