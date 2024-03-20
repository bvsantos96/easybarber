CREATE TABLE `user` (
	`id` int NOT NULL,
	`user_type_id` int NOT NULL,
	`email` varchar(255) NOT NULL,
	`password` varchar(255) NOT NULL,
	`country_mobile` varchar(10) NOT NULL,
	`mobile` varchar(15) NOT NULL,
	`token_expiration` DATETIME NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `user_type` (
	`id` int NOT NULL,
	`type` varchar(20) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `establishment` (
	`id` int NOT NULL,
	`owner_id` int NOT NULL,
	`name` varchar(255) NOT NULL,
	`description` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `employee` (
	`id` int NOT NULL,
	`user_id` int NOT NULL,
	`name` varchar(255) NOT NULL,
	`description` varchar(255) NOT NULL,
	PRIMARY KEY (`id`,`user_id`)
);

CREATE TABLE `appointment` (
	`id` int NOT NULL,
	`appointment_type_id` int NOT NULL,
	`employee_id` int NOT NULL,
	`establishment_id` int NOT NULL,
	`name` varchar(255) NOT NULL,
	`description` varchar(255) NOT NULL,
	`type` varchar(20) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `appointment_type` (
	`id` int NOT NULL,
	`type` varchar(20) NOT NULL,
	`description` varchar(100) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `establishment_staff` (
	`employee_id` int NOT NULL,
	`establishment_id` int NOT NULL,
	`admin` BOOLEAN NOT NULL
);

ALTER TABLE `user` ADD CONSTRAINT `user_fk0` FOREIGN KEY (`user_type_id`) REFERENCES `user_type`(`id`);

ALTER TABLE `establishment` ADD CONSTRAINT `establishment_fk0` FOREIGN KEY (`owner_id`) REFERENCES `user`(`id`);

ALTER TABLE `employee` ADD CONSTRAINT `employee_fk0` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);

ALTER TABLE `appointment` ADD CONSTRAINT `appointment_fk0` FOREIGN KEY (`appointment_type_id`) REFERENCES `appointment_type`(`id`);

ALTER TABLE `appointment` ADD CONSTRAINT `appointment_fk1` FOREIGN KEY (`employee_id`) REFERENCES `employee`(`id`);

ALTER TABLE `appointment` ADD CONSTRAINT `appointment_fk2` FOREIGN KEY (`establishment_id`) REFERENCES `establishment`(`id`);

ALTER TABLE `establishment_staff` ADD CONSTRAINT `establishment_staff_fk0` FOREIGN KEY (`employee_id`) REFERENCES `employee`(`id`);

ALTER TABLE `establishment_staff` ADD CONSTRAINT `establishment_staff_fk1` FOREIGN KEY (`establishment_id`) REFERENCES `establishment`(`id`);
