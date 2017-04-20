CREATE DATABASE product
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `product` (
  `seq` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `productId` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  `price` int(11) NOT NULL DEFAULT '0',
  `inventory` int(11) NOT NULL DEFAULT '0',
  `imagePath` varchar(1000) NOT NULL DEFAULT '',
  `description` text,
  `created` datetime NOT NULL,
  `updated` datetime DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

CREATE TABLE `raw_event` (
  `seq` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `identifier` varchar(8) NOT NULL DEFAULT '',
  `type` varchar(200) NOT NULL DEFAULT '',
  `version` bigint(20) NOT NULL,
  `payload` text NOT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;



CREATE TABLE `sequence` (
  `orderId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;