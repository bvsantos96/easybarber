-- MySQL dump 10.13  Distrib 8.3.0, for macos14.2 (arm64)
--
-- Host: 172.233.245.119    Database: easy_barber_testing
-- ------------------------------------------------------
-- Server version	8.0.36-0ubuntu0.23.10.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `employee_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9daqcqq2nrtbcr5xqeivvkorq` (`employee_id`),
  KEY `FKa8m1smlfsc8kkjn2t6wpdmysk` (`user_id`),
  CONSTRAINT `FK9daqcqq2nrtbcr5xqeivvkorq` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FKa8m1smlfsc8kkjn2t6wpdmysk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment_type`
--

DROP TABLE IF EXISTS `appointment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_type`
--

LOCK TABLES `appointment_type` WRITE;
/*!40000 ALTER TABLE `appointment_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `n_rating` smallint NOT NULL DEFAULT '0',
  `rating` double NOT NULL DEFAULT '0',
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `user_mobile_information` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qnc9fividdvf48rr1jtrcqkcs` (`user_mobile_information`),
  CONSTRAINT `FKksh4i96ag81kf84awktwksiur` FOREIGN KEY (`user_mobile_information`) REFERENCES `user` (`mobile_information`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (0,0,0,1,NULL,NULL),(1,0,0,2,NULL,'+351999999999'),(1,0,0,3,NULL,'+351900000000');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_image`
--

DROP TABLE IF EXISTS `employee_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_image` (
  `entity_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjqtgbx7ipqgo9j7mqh07tpuyv` (`entity_id`),
  CONSTRAINT `FKjqtgbx7ipqgo9j7mqh07tpuyv` FOREIGN KEY (`entity_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_image`
--

LOCK TABLES `employee_image` WRITE;
/*!40000 ALTER TABLE `employee_image` DISABLE KEYS */;
INSERT INTO `employee_image` VALUES (2,1,'https://d2zdpiztbgorvt.cloudfront.net/region1/us/807905/biz_photo/c2da6e290fa84b0392079ca2ae658f-pedro-barber-biz-photo-cf22ec40162841139be5358ccd8193-booksy.jpeg'),(3,3,'https://cdn.camberwellshopping.com.au/wp-content/uploads/2021/07/13111806/The-best-barbers-in-Camberwell.jpg'),(2,5,'https://www.ringmybarber.com/wp-content/uploads/2022/10/qualities-of-a-highly-professional-barber.jpg'),(3,6,'https://www.josephguinbarber.com/uploads/1/2/4/4/124499791/josephguinhome_orig.jpg');
/*!40000 ALTER TABLE `employee_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establishment`
--

DROP TABLE IF EXISTS `establishment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establishment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `n_votes` bigint DEFAULT NULL,
  `sum_votes` bigint DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `location` geometry DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establishment`
--

LOCK TABLES `establishment` WRITE;
/*!40000 ALTER TABLE `establishment` DISABLE KEYS */;
INSERT INTO `establishment` VALUES (1,0,0,NULL,'Henrique Barber Shop','Henrique Barber Shop',_binary '\æ\0\0\0\0\0ú³)\"c\"ÀúDžPC@'),(2,0,0,NULL,'Forum Almada Barber Shop','Forum Almada Barber Shop',_binary '\æ\0\0\0\0\0·\Ñ\0\ÞY\"À\npTC@');
/*!40000 ALTER TABLE `establishment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establishment_image`
--

DROP TABLE IF EXISTS `establishment_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establishment_image` (
  `entity_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdvuqxcsya8hxjtfsqqdtoy618` (`entity_id`),
  CONSTRAINT `FKdvuqxcsya8hxjtfsqqdtoy618` FOREIGN KEY (`entity_id`) REFERENCES `establishment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establishment_image`
--

LOCK TABLES `establishment_image` WRITE;
/*!40000 ALTER TABLE `establishment_image` DISABLE KEYS */;
INSERT INTO `establishment_image` VALUES (1,1,'https://us-en-cdn.square.ncms.io/content/uploads/2022/10/BlackCat3.jpg.jpeg'),(2,3,'https://img.freepik.com/premium-vector/barbershop-logo-barber-shop-logo-vector-template_664675-709.jpg'),(1,5,'https://assets-global.website-files.com/644a9d9ce529ef8812f82a28/647fb85c69e95444243ef9bd_Henley%27s%20Gentlemen%27s%20Grooming%20-%20Barbershop%20and%20Mens%20Grooming.webp'),(2,6,'https://images.squarespace-cdn.com/content/v1/6499eadde1c0a02a7d1be4ac/66036202-71d4-465f-b189-75fd80017d66/110A2577.jpg');
/*!40000 ALTER TABLE `establishment_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establishment_service`
--

DROP TABLE IF EXISTS `establishment_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establishment_service` (
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `price` double DEFAULT NULL,
  `establishment_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `service_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe2gcddysc6njum0xmu68gwjlq` (`establishment_id`),
  KEY `FKe7cftoweu00yb0j4go9606rlc` (`service_id`),
  CONSTRAINT `FKe2gcddysc6njum0xmu68gwjlq` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`),
  CONSTRAINT `FKe7cftoweu00yb0j4go9606rlc` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establishment_service`
--

LOCK TABLES `establishment_service` WRITE;
/*!40000 ALTER TABLE `establishment_service` DISABLE KEYS */;
INSERT INTO `establishment_service` VALUES (1,10,1,1,1),(1,5,1,2,2),(1,15,2,3,3),(1,10,2,4,1);
/*!40000 ALTER TABLE `establishment_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establishment_staff`
--

DROP TABLE IF EXISTS `establishment_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establishment_staff` (
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `approved` tinyint(1) NOT NULL DEFAULT '0',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `employee_id` bigint NOT NULL,
  `establishment_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FK34orwdgal0jk4542lxyvsfc4m` (`employee_id`),
  KEY `FK3lnmq3hv96sebpcwor21viw1t` (`establishment_id`),
  CONSTRAINT `FK34orwdgal0jk4542lxyvsfc4m` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK3lnmq3hv96sebpcwor21viw1t` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establishment_staff`
--

LOCK TABLES `establishment_staff` WRITE;
/*!40000 ALTER TABLE `establishment_staff` DISABLE KEYS */;
INSERT INTO `establishment_staff` VALUES (1,1,0,2,1,1),(1,1,0,3,2,2),(0,1,0,2,2,3);
/*!40000 ALTER TABLE `establishment_staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `employee_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `service_type_id` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX43gsn9m3vwqnwqctsqta642g5` (`service_type_id`),
  KEY `FK3xthwhrkfejag3mpvp5ep6ppu` (`employee_id`),
  CONSTRAINT `FK3xthwhrkfejag3mpvp5ep6ppu` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK8e4s0klc1xdmf3dwoy16k7fmi` FOREIGN KEY (`service_type_id`) REFERENCES `service_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (2,1,1,'Simple haircut','Haircut'),(2,2,2,'Simple beard trim','Beard'),(3,3,3,'Simple haircut and beard trim','Beard and haircut');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_image`
--

DROP TABLE IF EXISTS `service_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_image` (
  `entity_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaig970fq9p87bl3ee87fotd1x` (`entity_id`),
  CONSTRAINT `FKaig970fq9p87bl3ee87fotd1x` FOREIGN KEY (`entity_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_image`
--

LOCK TABLES `service_image` WRITE;
/*!40000 ALTER TABLE `service_image` DISABLE KEYS */;
INSERT INTO `service_image` VALUES (1,1,'https://cdn-fnknc.nitrocdn.com/jwqHRGAzpUgGskUSHlppNQzwuXgXIKwg/assets/images/optimized/rev-99e07b0/www.fashionbeans.com/wp-content/uploads/2023/08/smartcutzbarbers_manwithnumber2andskinfadehaircut-696x445.jpg'),(2,4,'https://i.ytimg.com/vi/KBKAIdtRinc/maxresdefault.jpg'),(3,7,'https://cdn11.bigcommerce.com/s-h7l2pcerei/product_images/uploaded_images/trimming-beard.jpg'),(1,10,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQg6C-koP0XN8yAeszXhRukicFHhQnSkhVQUrQABvhnKQ&s'),(1,11,'https://i.pinimg.com/736x/09/25/9d/09259d4ab3cbf58d8d09312d4c1816b8.jpg'),(2,12,'https://i.ytimg.com/vi/V4aG7_zsyrg/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLCQSDIXessxdD6pYgn5Wr5uI2AGcA'),(2,13,'https://cdn.thebeardclub.com/articles/Trim_Your_Beard_2_3202ea96-9f43-43af-bc17-81955f6ddabc_1920x.jpg?ixlib=imgixjs-4.0.1'),(3,14,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOfIzEg-rxJJg7GqAE9vsPYE9NaduGYpFsh0NDOR8DGw&s'),(3,15,'https://cdn.shopify.com/s/files/1/0013/3536/1603/files/Short-And-Shaped.jpg?v=1603734407');
/*!40000 ALTER TABLE `service_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_type`
--

DROP TABLE IF EXISTS `service_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `imageurl` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_type`
--

LOCK TABLES `service_type` WRITE;
/*!40000 ALTER TABLE `service_type` DISABLE KEYS */;
INSERT INTO `service_type` VALUES (1,'Simple haircut','/icons/categories/haircut.svg','Haircut'),(2,'Spa','/icons/categories/spa.svg','Spa'),(3,'Creambath','/icons/categories/creamBath.svg','Creambath'),(4,'Massage','/icons/categories/massage.svg','Massage');
/*!40000 ALTER TABLE `service_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token_expiration` datetime(6) DEFAULT NULL,
  `user_type_id` bigint DEFAULT NULL,
  `country_mobile` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) NOT NULL,
  `mobile_information` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3tbutmd8b2416d77g7dhhnri4` (`mobile_information`),
  KEY `IDXt7ccnc1wptnpgv6dnxbldtfn9` (`mobile_information`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,NULL,2,'+351',NULL,'999999999','+351999999999','Henrique','$argon2id$v=19$m=16384,t=2,p=1$96DdpWx/zBE5On3QDd9eOQ$izRnQbqyDCj9c5F1u4MSxxXgZiBWrQQYtk0ctqqglS4'),(2,NULL,2,'+351',NULL,'900000000','+351900000000','Amigo do Joao','$argon2id$v=19$m=16384,t=2,p=1$S0eLj45P6j2Rn/sPPc969g$NCoUSWNuIMQYhkRwszr+h6pzAam9k4vYWrqESIL3Xik'),(3,NULL,1,'+351',NULL,'927030780','+351927030780','Bruno Vicente dos Santos','$argon2id$v=19$m=16384,t=2,p=1$+xvL6t6IQb9Jxm52qDLOLQ$RnmrAKhvaAJXVUQ7yuiUtCDyEajqM4nvhWdZ4kIesTQ'),(4,NULL,1,'+351',NULL,'962844407','+351962844407','Filipe Santos','$argon2id$v=19$m=16384,t=2,p=1$LuigtRiAKXBhZVRbKx77HQ$/o3iDiTvmvLkW7yeH9vqE4/1XGOVPL66sODm6vJg2F4');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_type`
--

DROP TABLE IF EXISTS `user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_type`
--

LOCK TABLES `user_type` WRITE;
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` VALUES (1,'CLIENT'),(2,'EMPLOYEE');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-12 16:17:31
