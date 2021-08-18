-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.40-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.0.0.5989
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table hwcustomerserverapi_db.global_configs
CREATE TABLE IF NOT EXISTS `global_configs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(200) DEFAULT NULL,
  `config_value` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.hw_goods_delivery
CREATE TABLE IF NOT EXISTS `hw_goods_delivery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `order_goods_no` varchar(100) NOT NULL,
  `machine_code` varchar(50) DEFAULT NULL,
  `sales_status` char(2) DEFAULT NULL COMMENT '2=Finish delivering goods | 10=Delivering goods | 0 / 11 = Failed',
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.machine
CREATE TABLE IF NOT EXISTS `machine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `machine_code` varchar(100) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT '0',
  `channel_type` varchar(50) NOT NULL DEFAULT '0' COMMENT 'midtrans or xendit',
  PRIMARY KEY (`id`),
  UNIQUE KEY `machine_code` (`machine_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.qr_transaction
CREATE TABLE IF NOT EXISTS `qr_transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `machine_code` varchar(50) NOT NULL,
  `total_fee` decimal(10,0) NOT NULL,
  `goods_code` varchar(50) NOT NULL,
  `goods_protocol` varchar(50) NOT NULL,
  `goods_name` varchar(100) NOT NULL,
  `merchant_id` varchar(50) DEFAULT NULL,
  `channel_type` varchar(50) DEFAULT NULL,
  `channel_transaction_id` varchar(50) DEFAULT NULL,
  `channel_order_id` varchar(50) DEFAULT NULL,
  `channel_currency` varchar(5) DEFAULT NULL,
  `channel_transaction_time` datetime DEFAULT NULL,
  `channel_fraud_status` varchar(50) DEFAULT NULL,
  `channel_qr_code` varchar(150) DEFAULT NULL,
  `channel_deeplink` varchar(150) DEFAULT NULL,
  `qr_string` text,
  `payment_status` varchar(50) DEFAULT NULL,
  `settlement_time` datetime DEFAULT NULL,
  `error_message` text,
  `sys_update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1 COMMENT='This is Payment table of HW Customer Server API. This table will manage all payment request data from HW machine and the payment response from payment Channel/Gateway';

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.role
CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(150) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.sales_status
CREATE TABLE IF NOT EXISTS `sales_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.store_transaction
CREATE TABLE IF NOT EXISTS `store_transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `order_type` varchar(50) NOT NULL,
  `operate_code` varchar(50) NOT NULL,
  `payment_time` datetime NOT NULL,
  `original_amount` decimal(10,0) NOT NULL,
  `deposit_amount` decimal(10,0) NOT NULL,
  `payment_amount` decimal(10,0) NOT NULL,
  `machine_code` varchar(50) NOT NULL,
  `order_source` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='This is Transaction table of HW Customer Server API through for QR payment transaction (like Gopay). This table will manage all payment request data from HW machine and the payment response from payment Channel/Gateway';

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.store_transaction_detail
CREATE TABLE IF NOT EXISTS `store_transaction_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) DEFAULT NULL,
  `taste_id` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `good_id` varchar(50) DEFAULT NULL,
  `order_goods_no` varchar(50) DEFAULT NULL,
  `brewing_code` varchar(50) DEFAULT NULL,
  `order_price` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table hwcustomerserverapi_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL DEFAULT '0',
  `full_name` varchar(150) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_no` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `login_attempt` int(11) NOT NULL DEFAULT '0',
  `time_updated` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
