/*
MySQL - 5.1.30-community : Database - cas_test
*********************************************************************
*/
-- ----------------------------
-- Table structure for ly_res_user
-- ----------------------------
DROP TABLE IF EXISTS `cas_user`;
CREATE TABLE `cas_user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
    `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
    `email` varchar(32) DEFAULT '' COMMENT '邮箱',
    `age` tinyint(4) DEFAULT '0' COMMENT '年龄',
    `addr` varchar(64) DEFAULT '' COMMENT '住址',
    `phone` varchar(11) DEFAULT '' COMMENT '电话',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*Data for the table `cas_user` */

insert  into `cas_user`(`username`,`password`,`email`,`age`,`addr`,`phone`)
values
('admin','e10adc3949ba59abbe56e057f20f883e','admin@aliyun.com',18,'houpodelang','110'),
('doubi','doubi','505271051@qq.com',22,'beijin','110'),
('zhangsan','zhangsan','505271051@qq.com',32,'wuhan','110')


