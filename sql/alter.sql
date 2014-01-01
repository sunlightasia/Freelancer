DROP TABLE IF EXISTS `propertytype`;
CREATE TABLE `propertytype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(35) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `propertytype`(`name`) VALUES
('Flat'),('House'),('Castle');

CREATE TABLE `structureproperty` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_structure` int(11) NOT NULL,
  `id_property` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `structure`
ADD COLUMN isEnable character not null