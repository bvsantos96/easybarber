-- MySQL dump 10.13  Distrib 8.3.0, for macos14.2 (arm64)
--
-- Host: 172.233.245.119    Database: easy_barber_stagging_heavy
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
  `feedback` int DEFAULT NULL,
  `feedback_asked` tinyint(1) NOT NULL DEFAULT '0',
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
  KEY `IDXocj7ys3racy36d4tdauqvvqp2` (`user_id`),
  KEY `IDXm7ngu3uqckrnkf0x7qcbwhj8q` (`user_id`,`feedback_asked`),
  KEY `FK9daqcqq2nrtbcr5xqeivvkorq` (`employee_id`),
  KEY `FK995w9yqrlhf2jhiqvon7ndfn2` (`establishment_id`),
  KEY `FKc5vnhe960ftnpvt7pcwq5pd57` (`establishment_service_id`),
  CONSTRAINT `FK995w9yqrlhf2jhiqvon7ndfn2` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`),
  CONSTRAINT `FK9daqcqq2nrtbcr5xqeivvkorq` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FKa8m1smlfsc8kkjn2t6wpdmysk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKc5vnhe960ftnpvt7pcwq5pd57` FOREIGN KEY (`establishment_service_id`) REFERENCES `establishment_service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4015 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=501 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXbbkynu30go5e2pww6iggdktnb` (`entity_id`,`is_main`),
  KEY `IDXb1wodbu6fbd4faq15k313h9b0` (`entity_id`,`data`),
  CONSTRAINT `FKjqtgbx7ipqgo9j7mqh07tpuyv` FOREIGN KEY (`entity_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `idx_establishment_day_active_start_hour` (`establishment_id`,`day`,`active`,`start_hour`),
  KEY `FKopf7kqhml8g3efddqc6rki7jv` (`employee_id`),
  CONSTRAINT `FKdnicvgjeytvd7t7nf4yh9itj2` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`),
  CONSTRAINT `FKopf7kqhml8g3efddqc6rki7jv` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2946 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX368m4bnfqblu9ov75o8psxsqw` (`entity_id`,`is_main`),
  KEY `IDX95w699v45nsknxm1rvlxdssd2` (`entity_id`,`data`),
  CONSTRAINT `FKdvuqxcsya8hxjtfsqqdtoy618` FOREIGN KEY (`entity_id`) REFERENCES `establishment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=3644 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=501 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX916k1n69pqygq1ouva6geaab7` (`entity_id`,`is_main`),
  KEY `IDXixi75usg18jb8sl9l0g8ncccd` (`entity_id`,`data`),
  CONSTRAINT `FKaig970fq9p87bl3ee87fotd1x` FOREIGN KEY (`entity_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=1501 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_favorite`
--

DROP TABLE IF EXISTS `user_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_favorite` (
  `establishment_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`establishment_id`,`user_id`),
  KEY `FK2eajkverwrmx1fdy0oovpxrx0` (`user_id`),
  CONSTRAINT `FK2eajkverwrmx1fdy0oovpxrx0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK9sl1r4g5s15f0mqr6oapiyyae` FOREIGN KEY (`establishment_id`) REFERENCES `establishment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-23  0:16:18
