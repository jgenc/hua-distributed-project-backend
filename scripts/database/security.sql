-- This sql file is needed for the Spring DB to work normally. The server will automatically use these tables if they are created
-- in this specific manner 

USE `students`;

CREATE TABLE IF NOT EXISTS `user` (
 `username` varchar(50) NOT NULL,
 `password` varchar(100) NOT NULL,
 `enabled` tinyint(1) NOT NULL,
 PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `authorities` (
 `username` varchar(50) NOT NULL,
 `authority` varchar(50) NOT NULL,
 UNIQUE KEY `ix_auth_username` (`username`,`authority`),
 CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Password: pass123
INSERT INTO `user` (`username`, `password`, `enabled`) VALUES
   ('user', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', true),
   ('root', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', true);

INSERT INTO `authorities` (`username`, `authority`) VALUES
   ('root', 'ROLE_ADMIN'),
   ('argiris', 'ROLE_USER');
