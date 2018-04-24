CREATE TABLE `Spitter` (
  `id` bigint(20) NOT NULL auto_increment,
  `userName` varchar(64) NOT NULL COMMENT '用户姓名',
  `password` varchar(64) NOT NULL COMMENT '用户密码',
  `firstName` varchar(64) NOT NULL COMMENT '名字',
  `lastName` varchar(64) NOT NULL COMMENT '姓氏',
  `email` varchar(64)  COMMENT '邮箱',
  PRIMARY KEY (`id`)
);

alter table Spitter add constraint userName unique(userName);

insert into Spitter values (null, 'ynding', '111111', 'yanan', 'ding', '913690560@qq.com');

