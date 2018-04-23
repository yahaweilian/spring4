CREATE TABLE `Spittle` (
  `id_` bigint(20) NOT NULL AUTO_INCREMENT,
  `message` varchar(300),
  `time_` datetime DEFAULT CURRENT_TIMESTAMP ,
  `latitude` double precision,
  `longitude` double precision,
  PRIMARY KEY (`id_`)
);

 