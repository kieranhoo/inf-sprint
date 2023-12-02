-- MariaDB dump 10.19  Distrib 10.4.21-MariaDB, for osx10.10 (x86_64)
--
-- Host: database-2.ck0x1yxnzx9p.ap-southeast-1.rds.amazonaws.com    Database: document_management
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `document`
--



DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  `date_deleted` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dep_id_document` (`department_id`),
  CONSTRAINT `fk_dep_id_document` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `token_type_id` int NOT NULL,
  `revoked` tinyint(1) DEFAULT NULL,
  `expired` tinyint(1) DEFAULT NULL,
  `user_id` int NOT NULL,
  `token_category_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  KEY `fk_token_type_id` (`token_type_id`),
  KEY `fk_token_user_id` (`user_id`),
  KEY `fk_token_category_id` (`token_category_id`),
  CONSTRAINT `fk_token_category_id` FOREIGN KEY (`token_category_id`) REFERENCES `token_category` (`id`),
  CONSTRAINT `fk_token_type_id` FOREIGN KEY (`token_type_id`) REFERENCES `token_type` (`id`),
  CONSTRAINT `fk_token_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=351 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `token_category`
--

DROP TABLE IF EXISTS `token_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token_category_name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_category_name` (`token_category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `token_type`
--

DROP TABLE IF EXISTS `token_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token_type_name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_type_name` (`token_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_id_user_role` (`user_id`),
  KEY `fk_role_id_user_role` (`role_id`),
  CONSTRAINT `fk_role_id_user_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `fk_user_id_user_role` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int DEFAULT NULL,
  `user_name` varchar(30) NOT NULL,
  `email` varchar(100) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `date_deleted` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_user_id_document` (`department_id`),
  CONSTRAINT `fk_user_id_document` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `version`
--

DROP TABLE IF EXISTS `version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `version` (
  `id` int NOT NULL AUTO_INCREMENT,
  `document_id` int DEFAULT NULL,
  `url` varchar(300) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `note` varchar(200) DEFAULT NULL,
  `current_version` tinyint(1) DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_document_id_version` (`document_id`),
  CONSTRAINT `fk_document_id_version` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `version`
--

LOCK TABLES `version` WRITE;
/*!40000 ALTER TABLE `version` DISABLE KEYS */;
INSERT INTO `version` VALUES (1,1,'https://example.com/version/1','Version 1.0.0','Initial version',0,'2023-11-01'),(2,1,'https://example.com/version/2','Version 2.0.0','Updated content',0,'2023-11-02'),(3,2,'https://example.com/version/1','Version 1.0.0','Initial version',0,'2023-11-02'),(4,2,'https://example.com/version/2','Version 2.0.0','Updated content 1',0,'2023-11-02'),(5,2,'https://example.com/version/3','Version 3.0.0','Updated content 2',1,'2023-11-02'),(6,3,'https://example.com/version/1','Version 1.0.0','Initial version',0,'2023-11-03'),(7,3,'https://example.com/version/2','Version 2.0.0','Updated content ALA',1,'2023-11-03'),(8,4,'https://example.com/version/1','Version 1.0.0','Initial version',1,'2023-11-04'),(9,5,'https://example.com/version/1','Version 1.0.0','Initial version',1,'2023-11-05'),(10,6,'https://example.com/version/1','Version 1.0.0','Initial version',1,'2023-11-06'),(11,7,'https://example.com/version/1','Version 1.0.0','Initial version',1,'2023-11-07'),(12,8,'https://example.com/version/1','Version 1.0.0','Initial version',1,'2023-11-08'),(13,9,'https://example.com/version/1','Version 1.0.0','Initial version',1,'2023-11-09'),(14,10,'https://example.com/version/1','Version 1.0.0','Initial version',1,'2023-11-10'),(15,11,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2020_MT_KHM.pdf?alt=media&token=37c44d6d-dc25-4cde-bcc2-30a2a05f753f','Version 1.0.0','12312312',1,'2023-11-08'),(35,1,'https://example.com/version/2','Version 2.0.0','Updated content',0,'2023-11-08'),(36,1,NULL,'Version 2.0.0','Updated content',0,'2023-11-08'),(38,1,NULL,'Version 2.0.0','Updated content oke',0,'2023-11-08'),(39,1,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 2.0.0','Updated content oke 123',0,'2023-11-08'),(40,1,NULL,'Version 2.0.0','Updated content oke 12345',0,'2023-11-08'),(43,12,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=e1094960-ed15-4fc8-9132-0fb702fff92d','Version 1.0.0','test',1,'2023-11-09'),(44,13,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=9cecb763-fc75-4338-b700-604a73507463','Version 1.0.0','hello',0,'2023-11-09'),(45,14,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=8743956d-1131-40a7-8064-a5defdf4bdfe','Version 1.0.0','doc',1,'2023-11-09'),(46,15,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 1.0.0','Initial version',1,'2023-11-01'),(47,16,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 2.0.0','Updated content',1,'2023-11-02'),(48,17,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 1.0.0','Initial version',1,'2023-11-02'),(49,18,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 2.0.0','Updated content 1',1,'2023-11-02'),(50,19,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 3.0.0','Updated content 2',1,'2023-11-02'),(51,20,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 1.0.0','Initial version',1,'2023-11-03'),(52,21,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 2.0.0','Updated content ALA',1,'2023-11-03'),(53,22,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 1.0.0','Initial version',0,'2023-11-04'),(54,23,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 1.0.0','Initial version',0,'2023-11-05'),(55,24,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 1.0.0','Initial version',1,'2023-11-06'),(56,25,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=b0737e4f-7527-4e38-a31a-216285e95866','Version 1.0.0','Initial version',1,'2023-11-07'),(57,23,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2020_MT_KHM.pdf?alt=media&token=b273bc73-f59a-4674-bb65-e4df35f85d18','Version 1.0.1','Test update',0,'2023-11-11'),(58,26,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2020_MT_KHM.pdf?alt=media&token=d57940a2-33a0-46e8-922e-20bc79e0d0a4','Version 1.0.0','Intital version',0,'2023-11-11'),(59,26,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2022_HC_HC1.pdf?alt=media&token=b4a146b7-05ba-4bf2-a3a5-34442ab4df31','Version 1.0.1','Intital version',1,'2023-11-11'),(60,13,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2021_HC_CSH.pdf?alt=media&token=a2113fde-95f8-42eb-88ad-0075a6c6bec3','Version 1.0.1','updated',0,'2023-11-11'),(61,27,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2020_MT_KHM.pdf?alt=media&token=6494f70e-c905-4cd8-8e97-63dd843b6f19','Version 1.0.0','heh',0,'2023-11-11'),(62,27,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2022_HC_HC1.pdf?alt=media&token=913a23df-bc92-4547-9a87-55dcae0819e9','Version 1.0.1','hehe update',1,'2023-11-11'),(63,23,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F2020_MT_KHM.pdf?alt=media&token=44663086-3e3a-4f57-98e5-69d9c656d8a9','Version 1.0.2','Update new file',1,'2023-11-11'),(64,13,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F00_Course%20outline_updated_1509.pdf?alt=media&token=cc87cbc4-5dcb-4a69-a994-0e76ef6bed6c','Version 1.0.2','updated new version',1,'2023-11-11'),(65,28,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2FDatabase%20Security%20-%20Paper%20-%20HK231.pdf?alt=media&token=21b1bdb8-38ae-48a4-a540-57b366983ad6','Version 1.0.0','Need to be done before 11th November, at 12h30',1,'2023-11-11'),(66,22,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F4_Black-box%20testing.pdf?alt=media&token=396f9e9e-a82e-41e8-be92-6ed5d237bbf1','Version 1.0.1','Updated version',1,'2023-11-11'),(67,29,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F4_Black-box%20testing.pdf?alt=media&token=932550e9-1978-4bbe-9494-e39c8c3d7db5','Version 1.0.0','initial',1,'2023-11-11'),(68,30,'https://firebasestorage.googleapis.com/v0/b/kotlin-ecommerce-a7e7c.appspot.com/o/files%2F4_Black-box%20testing.pdf?alt=media&token=d8229829-c3af-438e-9f5e-06b7b41eed5f','Version 1.0.0','dsdsds',1,'2023-11-11');
/*!40000 ALTER TABLE `version` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-27 19:25:31
