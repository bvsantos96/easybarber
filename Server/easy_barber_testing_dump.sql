-- MySQL dump 10.13  Distrib 8.3.0, for macos14.2 (arm64)
--
-- Host: 172.233.245.119    Database: easy_barber_testing
-- ------------------------------------------------------
-- Server version	8.0.37-0ubuntu0.23.10.2

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
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `confirmed` tinyint(1) NOT NULL DEFAULT '1',
  `date` date DEFAULT NULL,
  `reminded` tinyint(1) NOT NULL DEFAULT '0',
  `time` time(6) DEFAULT NULL,
  `employee_id` bigint NOT NULL,
  `establishment_id` bigint NOT NULL,
  `establishment_service_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `non_registered_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9daqcqq2nrtbcr5xqeivvkorq` (`employee_id`),
  KEY `FK995w9yqrlhf2jhiqvon7ndfn2` (`establishment_id`),
  KEY `FKc5vnhe960ftnpvt7pcwq5pd57` (`establishment_service_id`),
  KEY `FKa8m1smlfsc8kkjn2t6wpdmysk` (`user_id`),
  CONSTRAINT `FK995w9yqrlhf2jhiqvon7ndfn2` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`),
  CONSTRAINT `FK9daqcqq2nrtbcr5xqeivvkorq` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FKa8m1smlfsc8kkjn2t6wpdmysk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKc5vnhe960ftnpvt7pcwq5pd57` FOREIGN KEY (`establishment_service_id`) REFERENCES `establishment_service` (`id`)
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
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `id` bigint NOT NULL AUTO_INCREMENT,
  `n_votes` bigint NOT NULL,
  `sum_votes` bigint NOT NULL,
  `user` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b1od27kmks86dw2jpm4kwdryk` (`user`),
  CONSTRAINT `FKjdyul4cmndt1b48vw1166bee5` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (0,1,0,0,NULL,NULL),(1,2,0,0,2,NULL),(1,3,0,0,3,NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_image`
--

DROP TABLE IF EXISTS `employee_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_image` (
  `is_main` tinyint(1) NOT NULL DEFAULT '0',
  `entity_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXbbkynu30go5e2pww6iggdktnb` (`entity_id`,`is_main`),
  KEY `IDXb1wodbu6fbd4faq15k313h9b0` (`entity_id`,`data`),
  CONSTRAINT `FKjqtgbx7ipqgo9j7mqh07tpuyv` FOREIGN KEY (`entity_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_image`
--

LOCK TABLES `employee_image` WRITE;
/*!40000 ALTER TABLE `employee_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_schedule`
--

DROP TABLE IF EXISTS `employee_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_schedule` (
  `active` bit(1) DEFAULT NULL,
  `day` tinyint DEFAULT NULL,
  `end_hour` time(6) DEFAULT NULL,
  `start_hour` time(6) DEFAULT NULL,
  `employee_id` bigint NOT NULL,
  `establishment_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKopf7kqhml8g3efddqc6rki7jv` (`employee_id`),
  KEY `FKdnicvgjeytvd7t7nf4yh9itj2` (`establishment_id`),
  CONSTRAINT `FKdnicvgjeytvd7t7nf4yh9itj2` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`),
  CONSTRAINT `FKopf7kqhml8g3efddqc6rki7jv` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_schedule`
--

LOCK TABLES `employee_schedule` WRITE;
/*!40000 ALTER TABLE `employee_schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establishment`
--

DROP TABLE IF EXISTS `establishment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establishment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `n_votes` bigint NOT NULL,
  `sum_votes` bigint NOT NULL,
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
INSERT INTO `establishment` VALUES (1,0,0,'Rua Ramiro Ferrao 43, 2805-356','Henrique Barber Shop','Henrique Barber Shop',_binary '\æ\0\0\0\0\0ú³)\"c\"ÀúDžPC@'),(2,0,0,'Rua Ramiro Ferrao 43, 2805-356','Forum Almada Barber Shop','Forum Almada Barber Shop',_binary '\æ\0\0\0\0\0·\Ñ\0\ÞY\"À\npTC@');
/*!40000 ALTER TABLE `establishment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establishment_image`
--

DROP TABLE IF EXISTS `establishment_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establishment_image` (
  `is_main` tinyint(1) NOT NULL DEFAULT '0',
  `entity_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX368m4bnfqblu9ov75o8psxsqw` (`entity_id`,`is_main`),
  KEY `IDX95w699v45nsknxm1rvlxdssd2` (`entity_id`,`data`),
  CONSTRAINT `FKdvuqxcsya8hxjtfsqqdtoy618` FOREIGN KEY (`entity_id`) REFERENCES `establishment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establishment_image`
--

LOCK TABLES `establishment_image` WRITE;
/*!40000 ALTER TABLE `establishment_image` DISABLE KEYS */;
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
-- Table structure for table `establishment_service_employee`
--

DROP TABLE IF EXISTS `establishment_service_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establishment_service_employee` (
  `employee_id` bigint NOT NULL,
  `establishment_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `service_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK455e7ijpacj566hva0m6ymla7` (`employee_id`),
  KEY `FKe7ervykqx44734smntb5lbc2t` (`establishment_id`),
  KEY `FKfqammx81imvgsdwcsj4m07vhs` (`service_id`),
  CONSTRAINT `FK455e7ijpacj566hva0m6ymla7` FOREIGN KEY (`employee_id`) REFERENCES `establishment_staff` (`id`),
  CONSTRAINT `FKe7ervykqx44734smntb5lbc2t` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`),
  CONSTRAINT `FKfqammx81imvgsdwcsj4m07vhs` FOREIGN KEY (`service_id`) REFERENCES `establishment_service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establishment_service_employee`
--

LOCK TABLES `establishment_service_employee` WRITE;
/*!40000 ALTER TABLE `establishment_service_employee` DISABLE KEYS */;
INSERT INTO `establishment_service_employee` VALUES (1,1,1,1),(1,1,2,2),(2,2,3,3),(3,2,4,3),(3,2,5,4);
/*!40000 ALTER TABLE `establishment_service_employee` ENABLE KEYS */;
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
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location` (
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `selected` bit(1) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXe5jwd37ob25i4e7jwownmu08w` (`user_id`),
  CONSTRAINT `FKeua4vn06qu0iq9d32qnmuhqkl` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_exception`
--

DROP TABLE IF EXISTS `schedule_exception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_exception` (
  `active` bit(1) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `day` tinyint DEFAULT NULL,
  `end_hour` time(6) DEFAULT NULL,
  `start_hour` time(6) DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `establishment_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `IDXhg5cw9l1jytwj3fr72ge5r38g` (`employee_id`),
  KEY `IDXc7ueuc2o997usil2ewdc4lupa` (`employee_id`,`date`),
  KEY `FKstfqw5nn4jg2djanwcmxom745` (`establishment_id`),
  CONSTRAINT `FKcgandf16c4r4sb03tb3aotqiy` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FKstfqw5nn4jg2djanwcmxom745` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_exception`
--

LOCK TABLES `schedule_exception` WRITE;
/*!40000 ALTER TABLE `schedule_exception` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_exception` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `duration` int NOT NULL,
  `employee_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `service_type_id` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX43gsn9m3vwqnwqctsqta642g5` (`service_type_id`),
  KEY `IDXcsgprwtglgj0cf507qpt5oyx5` (`employee_id`),
  CONSTRAINT `FK3xthwhrkfejag3mpvp5ep6ppu` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK8e4s0klc1xdmf3dwoy16k7fmi` FOREIGN KEY (`service_type_id`) REFERENCES `service_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (30,2,1,1,'Simple haircut','Haircut'),(15,2,2,2,'Simple beard trim','Beard'),(45,3,3,3,'Simple haircut and beard trim','Beard and haircut');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_image`
--

DROP TABLE IF EXISTS `service_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_image` (
  `is_main` tinyint(1) NOT NULL DEFAULT '0',
  `entity_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX916k1n69pqygq1ouva6geaab7` (`entity_id`,`is_main`),
  KEY `IDXixi75usg18jb8sl9l0g8ncccd` (`entity_id`,`data`),
  CONSTRAINT `FKaig970fq9p87bl3ee87fotd1x` FOREIGN KEY (`entity_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_image`
--

LOCK TABLES `service_image` WRITE;
/*!40000 ALTER TABLE `service_image` DISABLE KEYS */;
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
INSERT INTO `user` VALUES (1,NULL,'+351',NULL,'962844407','+351962844407','Filipe Santos','$argon2id$v=19$m=16384,t=2,p=1$oAlu7tN51R+AkOrV6cZgGg$8uInDJH/hdLXSnkaJWMwS5v/taHLL/qmzdFabDj4tTw'),(2,NULL,'+351',NULL,'999999999','+351999999999','Henrique','$argon2id$v=19$m=16384,t=2,p=1$JLitC2vAQOp3D23QS2ByFw$7gIrmH+8O/BKLb5jy+cCWJwAtMGP3HbuiFWwR8vghxU'),(3,NULL,'+351',NULL,'900000000','+351900000000','Amigo do Joao','$argon2id$v=19$m=16384,t=2,p=1$KwV+CYvHXxHUZqv3oAti0A$trp3AmBlpLvmv1aV+QxxfnmQ713ifgaWSHrHCLtrLwE'),(4,NULL,'+1',NULL,'999999999','+1999999999','System Admin','$argon2id$v=19$m=16384,t=2,p=1$0BgsPS5GOi/MisYGk53s6w$Swvaa8MaAVbItJ5cV83aRaLWh964jCzoQ42LFLjJGQA');
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
  `user_id` bigint DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj0whdmtccunmsfxctomsgp1vn` (`user_id`),
  CONSTRAINT `FKj0whdmtccunmsfxctomsgp1vn` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_type`
--

LOCK TABLES `user_type` WRITE;
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` VALUES (1,NULL,'LOCKED'),(2,NULL,'SYSTEM_ADMIN'),(3,NULL,'CLIENT'),(4,NULL,'EMPLOYEE');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_user_type`
--

DROP TABLE IF EXISTS `user_user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_user_type` (
  `user_id` bigint NOT NULL,
  `user_type_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`user_type_id`),
  KEY `FKey2ndeuy85hmce2rnahxyr32v` (`user_type_id`),
  CONSTRAINT `FKdhd4d48ssl8on7mm3scr04s50` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKey2ndeuy85hmce2rnahxyr32v` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_user_type`
--

LOCK TABLES `user_user_type` WRITE;
/*!40000 ALTER TABLE `user_user_type` DISABLE KEYS */;
INSERT INTO `user_user_type` VALUES (4,2),(1,3),(2,3),(3,3),(4,3),(2,4),(3,4);
/*!40000 ALTER TABLE `user_user_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_code`
--

DROP TABLE IF EXISTS `verification_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verification_code` (
  `code` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) NOT NULL,
  PRIMARY KEY (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_code`
--

LOCK TABLES `verification_code` WRITE;
/*!40000 ALTER TABLE `verification_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `verification_code` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-29 21:41:51
