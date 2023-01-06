USE `hb_student_tracker`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `teacher_profile`;

CREATE TABLE `teacher_profile` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`youtube_channel` varchar(128) DEFAULT NULL,
`hobby` varchar(45) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `teacher`;

CREATE TABLE `teacher` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`first_name` varchar(45) DEFAULT NULL,
`last_name` varchar(45) DEFAULT NULL,
`email` varchar(45) DEFAULT NULL,
`teacher_profile_id` int(11) DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `FK_DETAIL_idx` (`teacher_profile_id`),
CONSTRAINT `FK_DETAIL` FOREIGN KEY (`teacher_profile_id`) REFERENCES `teacher_profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE `course` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`title` varchar(128) DEFAULT NULL,
`teacher_id` int(11) DEFAULT NULL,

PRIMARY KEY (`id`),

UNIQUE KEY `TITLE_UNIQUE` (`title`),

KEY `FK_TEACHER_idx` (`teacher_id`),

CONSTRAINT `FK_TEACHER`
FOREIGN KEY (`teacher_id`)
REFERENCES `teacher` (`id`)

ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE `course_student` (
`course_id` int(11) NOT NULL,
`student_id` int(11) NOT NULL,

PRIMARY KEY (`course_id`,`student_id`),

KEY `FK_STUDENT_idx` (`student_id`),

CONSTRAINT `FK_COURSE_05` FOREIGN KEY (`course_id`)
REFERENCES `course` (`id`)
ON DELETE NO ACTION ON UPDATE NO ACTION,

CONSTRAINT `FK_STUDENT` FOREIGN KEY (`student_id`)
REFERENCES `student` (`id`)
ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
