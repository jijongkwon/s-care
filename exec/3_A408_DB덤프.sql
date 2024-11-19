-- MySQL dump 10.13  Distrib 8.4.1, for Win64 (x86_64)
--
-- Host: k11a408.p.ssafy.io    Database: scare
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Table structure for table `daily_stress`
--

DROP TABLE IF EXISTS `daily_stress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `daily_stress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `recorded_at` date NOT NULL,
  `stress` int NOT NULL,
  `member_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgdu6e8cu4ctjfvsyfdrqogl4p` (`member_id`),
  CONSTRAINT `FKgdu6e8cu4ctjfvsyfdrqogl4p` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32320 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_stress`
--

LOCK TABLES `daily_stress` WRITE;
/*!40000 ALTER TABLE `daily_stress` DISABLE KEYS */;
INSERT INTO `daily_stress` VALUES (1,'2024-11-11 21:19:58.550253','2024-11-11 21:19:58.550253','2024-11-10',36,8),(2,'2024-11-13 10:12:13.769833','2024-11-13 10:12:13.769833','2024-11-11',50,8),(3,'2024-11-15 10:38:02.867746','2024-11-15 10:38:02.867746','2024-11-14',44,5),(5,'2024-11-17 14:28:59.651854','2024-11-17 14:28:59.651854','2024-11-16',32,5),(32268,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-01',42,3),(32269,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-02',59,3),(32270,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-03',57,3),(32271,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-04',48,3),(32272,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-05',59,3),(32273,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-06',41,3),(32274,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-07',18,3),(32275,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-08',60,3),(32276,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-09',32,3),(32277,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-10',21,3),(32278,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-11',53,3),(32279,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-12',36,3),(32280,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-13',14,3),(32281,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-14',53,3),(32282,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-15',10,3),(32283,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-16',33,3),(32284,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-17',25,3),(32285,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-18',14,3),(32286,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-19',40,3),(32287,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-20',44,3),(32288,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-21',40,3),(32289,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-22',57,3),(32290,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-23',56,3),(32291,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-24',47,3),(32292,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-25',60,3),(32293,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-26',45,3),(32294,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-27',37,3),(32295,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-28',39,3),(32296,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-29',23,3),(32297,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-30',38,3),(32298,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-10-31',12,3),(32299,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-01',38,3),(32300,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-02',44,3),(32301,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-03',43,3),(32302,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-04',24,3),(32303,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-05',32,3),(32304,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-06',28,3),(32305,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-07',35,3),(32306,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-08',29,3),(32307,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-09',30,3),(32308,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-10',56,3),(32309,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-11',27,3),(32310,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-12',60,3),(32311,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-13',58,3),(32312,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-14',47,3),(32313,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-15',51,3),(32314,'2024-11-17 17:07:16.000000','2024-11-17 17:07:16.000000','2024-11-16',54,3),(32316,'2024-11-18 10:29:04.088724','2024-11-18 10:29:04.088724','2024-11-17',50,3),(32317,'2024-11-19 08:44:04.935808','2024-11-19 08:44:04.935808','2024-11-18',48,14),(32318,'2024-11-19 08:49:50.655987','2024-11-19 08:49:50.655987','2024-11-18',40,9),(32319,'2024-11-19 09:11:33.414718','2024-11-19 09:11:33.414718','2024-11-18',40,3);
/*!40000 ALTER TABLE `daily_stress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `provider` enum('GOOGLE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ROLE_ADMIN','ROLE_USER') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'2024-11-01 16:18:09.141177','2024-11-01 16:18:09.141177','user@example.com','exampleName','https://example.com','GOOGLE','ACTIVE','ROLE_USER'),(2,'2024-11-04 13:28:44.614731','2024-11-06 13:02:17.677129','ide05484@gmail.com','김도은','https://lh3.googleusercontent.com/a/ACg8ocLxf6PAZYrED3kXBoFpGzq9r686zhdKV4H50Odxis_bbvHENQ=s96-c','GOOGLE','INACTIVE','ROLE_USER'),(3,'2024-11-04 15:24:33.559769','2024-11-11 15:11:47.871676','ssafy.gpt408@gmail.com','GPT','https://lh3.googleusercontent.com/a/ACg8ocI3nKhnr-NpjIbnrUHjN3xYl94QFQkEEsxWLYFPYicgxqs7Lw=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(4,'2024-11-05 12:45:32.514105','2024-11-05 12:45:32.514105','wlwhdrnjs1@gmail.com','지종권','https://lh3.googleusercontent.com/a/ACg8ocLrqOfE7GmxmPR3BowLoB_w2GLjfrjvDCUEYbmunCO1WCcg9Q=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(5,'2024-11-06 08:51:30.714485','2024-11-06 08:51:30.714485','rladpgns00@gmail.com','김예훈','https://lh3.googleusercontent.com/a/ACg8ocLQPgW7hxveJicWClrcxgtDwOrSIWW5zJzpBRHJXBxluzcR7uw=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(6,'2024-11-06 13:07:24.349185','2024-11-06 13:07:27.498531','ide05484@gmail.com','김도은','https://lh3.googleusercontent.com/a/ACg8ocLxf6PAZYrED3kXBoFpGzq9r686zhdKV4H50Odxis_bbvHENQ=s96-c','GOOGLE','INACTIVE','ROLE_USER'),(7,'2024-11-06 13:07:32.588963','2024-11-06 13:07:35.963992','ide05484@gmail.com','김도은','https://lh3.googleusercontent.com/a/ACg8ocLxf6PAZYrED3kXBoFpGzq9r686zhdKV4H50Odxis_bbvHENQ=s96-c','GOOGLE','INACTIVE','ROLE_USER'),(8,'2024-11-06 13:17:45.279807','2024-11-06 13:17:45.279807','ide05484@gmail.com','김도은','https://lh3.googleusercontent.com/a/ACg8ocLxf6PAZYrED3kXBoFpGzq9r686zhdKV4H50Odxis_bbvHENQ=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(9,'2024-11-08 13:23:12.233488','2024-11-08 13:23:12.233488','simhani1@gmail.com','심종한','https://lh3.googleusercontent.com/a/ACg8ocKbaKCDgrk_s3qGMzIVvrYfb9WIccGuHawo8pCcD4gKbqHs_w=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(10,'2024-11-13 15:23:50.937770','2024-11-13 15:23:50.937770','kimyeonwook511@gmail.com','김연욱','https://lh3.googleusercontent.com/a/ACg8ocLK2euIep68h3H0VsE26KSKnjXRa_S1_JFZnJCx3V9KnCiirQ=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(11,'2024-11-14 14:47:37.552400','2024-11-14 14:47:37.552400','951goldenkiwi@gmail.com','박성혁','https://lh3.googleusercontent.com/a/ACg8ocISLOJBoSfUMwJlItt7hTPwQkH1v3gN_Y7YNh50oJue8hCpOAE=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(12,'2024-11-14 16:01:13.909058','2024-11-14 16:01:13.909058','jofrancis.46600@gmail.com','Jo Francis','https://lh3.googleusercontent.com/a/ACg8ocKpS1XVYyQlPV7KkTZPWY02JtHIF7Oi-81T1M3vGf5nwpJGxQ=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(13,'2024-11-15 15:52:04.767510','2024-11-15 15:52:04.767510','jeromemiller.52029@gmail.com','Jerome Miller','https://lh3.googleusercontent.com/a/ACg8ocJwCrVB4E-m_chSrK0mvowLfoDKYNHN7LT4FNHi1Shj2UiWqA=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(14,'2024-11-18 10:30:18.444630','2024-11-18 10:30:18.444630','hslee0912@gmail.com','Hyunseok Lee','https://lh3.googleusercontent.com/a/ACg8ocI7XSBL4TBrh73sOi4PGhEBZp6EpQ4FrcaRxKO9vGYgV-dWmM0=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(15,'2024-11-18 13:22:47.110819','2024-11-18 13:22:47.110819','ssafycoachdb@gmail.com','이담비','https://lh3.googleusercontent.com/a/ACg8ocJ83hY5Sqrr7DNV1uJjzLHwPqn_3h5zMctHfdoDBvuRL9ZxvmA=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(16,'2024-11-18 13:40:57.754587','2024-11-18 13:40:57.754587','irvinlee.68258@gmail.com','Irvin Lee','https://lh3.googleusercontent.com/a/ACg8ocI5tGuTFr2hW3KuFApG9RtU-bQbnP0GCTzzt2r1hVoWKWwd_Q=s96-c','GOOGLE','ACTIVE','ROLE_USER'),(17,'2024-11-18 16:33:10.100021','2024-11-18 16:33:10.100021','coachtest11th@gmail.com','coach coach','https://lh3.googleusercontent.com/a/ACg8ocLLDVHwiwnzy_cyhSI2qLFCUd--pA5A1NeMTRx6D4MqbgP4EQ=s96-c','GOOGLE','ACTIVE','ROLE_USER');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet`
--

DROP TABLE IF EXISTS `pet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet` (
  `pet_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet`
--

LOCK TABLES `pet` WRITE;
/*!40000 ALTER TABLE `pet` DISABLE KEYS */;
/*!40000 ALTER TABLE `pet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_collection`
--

DROP TABLE IF EXISTS `pet_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_collection` (
  `pet_collection_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  `pet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`pet_collection_id`),
  KEY `FKdbdwl7l48ewl0n6kgi77ttx5w` (`member_id`),
  KEY `FK2q9tpu4msjxsdbrk1y5hcuxuy` (`pet_id`),
  CONSTRAINT `FK2q9tpu4msjxsdbrk1y5hcuxuy` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`pet_id`),
  CONSTRAINT `FKdbdwl7l48ewl0n6kgi77ttx5w` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_collection`
--

LOCK TABLES `pet_collection` WRITE;
/*!40000 ALTER TABLE `pet_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `pet_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `walking_course`
--

DROP TABLE IF EXISTS `walking_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `walking_course` (
  `walking_course_id` bigint NOT NULL AUTO_INCREMENT,
  `end_idx` int DEFAULT NULL,
  `finished_at` datetime(6) NOT NULL,
  `max_stress` double NOT NULL,
  `min_stress` double NOT NULL,
  `start_idx` int NOT NULL,
  `started_at` datetime(6) NOT NULL,
  `healing_stress_avg` double NOT NULL,
  `member_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`walking_course_id`),
  KEY `FKp781o9v5vnbnqdb63xnobrkic` (`member_id`),
  CONSTRAINT `FKp781o9v5vnbnqdb63xnobrkic` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `walking_course`
--

LOCK TABLES `walking_course` WRITE;
/*!40000 ALTER TABLE `walking_course` DISABLE KEYS */;
INSERT INTO `walking_course` VALUES (102,0,'2024-11-15 17:40:21.000000',60,32,0,'2024-11-15 17:39:48.000000',0,5,'2024-11-15 17:40:22.677900','2024-11-15 17:40:22.677900'),(103,0,'2024-11-15 17:40:21.000000',60,32,0,'2024-11-15 17:39:48.000000',0,5,'2024-11-15 18:04:15.049550','2024-11-15 18:04:15.049550'),(104,0,'2024-11-15 17:40:21.000000',60,32,0,'2024-11-15 17:39:48.000000',0,5,'2024-11-15 18:04:37.519730','2024-11-15 18:04:37.519730'),(105,0,'2024-11-15 18:34:39.000000',46,24,0,'2024-11-15 18:34:14.000000',0,5,'2024-11-15 18:34:40.801944','2024-11-15 18:34:40.801944'),(106,0,'2024-11-15 18:35:28.000000',52,28,0,'2024-11-15 18:34:58.000000',0,5,'2024-11-15 18:35:30.356640','2024-11-15 18:35:30.356640'),(107,0,'2024-11-16 10:31:24.000000',60,40,0,'2024-11-16 10:30:37.000000',0,5,'2024-11-16 10:31:25.204360','2024-11-16 10:31:25.204360'),(112,7,'2024-11-18 11:54:21.000000',60,44,1,'2024-11-18 11:46:08.000000',52.857142857142854,5,'2024-11-18 11:54:27.592111','2024-11-18 11:54:27.592111'),(115,0,'2024-11-18 15:43:37.000000',0,0,0,'2024-11-18 15:36:39.000000',0,5,'2024-11-18 15:43:39.034939','2024-11-18 15:43:39.034939'),(116,0,'2024-11-18 16:01:07.000000',0,0,0,'2024-11-18 15:55:10.000000',0,15,'2024-11-18 16:01:08.663236','2024-11-18 16:01:08.663236'),(117,0,'2024-11-18 16:08:04.000000',0,0,0,'2024-11-18 16:02:47.000000',0,15,'2024-11-18 16:08:05.808862','2024-11-18 16:08:05.808862'),(118,149,'2024-11-18 16:57:32.000000',60,36,75,'2024-11-18 16:51:27.000000',53.04000000000008,5,'2024-11-18 16:57:43.792264','2024-11-18 16:57:43.792264'),(119,309,'2024-11-18 17:38:51.000000',60,40,207,'2024-11-18 17:31:19.000000',50.757281553398,3,'2024-11-18 17:39:10.764431','2024-11-18 17:39:10.764431'),(121,0,'2024-11-18 18:13:47.000000',50,18,0,'2024-11-18 18:06:07.000000',35.800000000000004,5,'2024-11-18 18:13:50.572318','2024-11-18 18:13:50.572318'),(122,149,'2024-11-18 19:36:42.000000',48,16,77,'2024-11-18 19:31:06.000000',31.78082191780823,3,'2024-11-18 19:36:54.846516','2024-11-18 19:36:54.846516');
/*!40000 ALTER TABLE `walking_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'scare'
--
/*!50003 DROP PROCEDURE IF EXISTS `InsertDummyDataForDailyStress` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`scare`@`%` PROCEDURE `InsertDummyDataForDailyStress`()
BEGIN
    -- 변수 선언은 BEGIN 블록의 가장 위에서 해야 합니다.
    DECLARE cur_date DATE DEFAULT '2024-10-01';
    DECLARE end_date DATE DEFAULT current_date();
    DECLARE random_stress INT;

    -- WHILE 루프 시작
    WHILE cur_date <= end_date DO
        -- 10 ~ 60 사이의 랜덤값 생성
        SET random_stress = FLOOR(10 + (RAND() * (60 - 10 + 1)));

        -- 데이터 삽입
        INSERT INTO daily_stress (created_at, updated_at, recorded_at, stress, member_id)
        VALUES (NOW(), NOW(), cur_date, random_stress, 3);

        -- 날짜를 하루씩 증가
        SET cur_date = DATE_ADD(cur_date, INTERVAL 1 DAY);
    END WHILE;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-19  9:23:59
