-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: libreta_ahorros
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `libreta`
--

DROP TABLE IF EXISTS `libreta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libreta` (
  `id_libreta` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `saldo_actual` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id_libreta`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libreta`
--

LOCK TABLES `libreta` WRITE;
/*!40000 ALTER TABLE `libreta` DISABLE KEYS */;
INSERT INTO `libreta` VALUES (1,'Despedida-Ana',0.00),(2,'Despedida',0.00),(7,'Hogar-Compartido',-150.00),(10,'No borrar',5661156.00),(11,'Comunion',-200.00),(12,'Casa',-150.00),(13,'Vacaciones',1293.00),(14,'2027',0.00);
/*!40000 ALTER TABLE `libreta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimiento`
--

DROP TABLE IF EXISTS `movimiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimiento` (
  `id_movimiento` int NOT NULL AUTO_INCREMENT,
  `id_libreta_fk` int NOT NULL,
  `fecha` date NOT NULL,
  `concepto` varchar(100) NOT NULL,
  `cantidad` decimal(10,2) NOT NULL,
  `tipo` enum('Ingreso','Gasto') NOT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `procedencia` varchar(100) DEFAULT NULL,
  `metodo_pago` varchar(100) DEFAULT NULL,
  `responsable` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_movimiento`),
  KEY `id_libreta_fk` (`id_libreta_fk`),
  CONSTRAINT `movimiento_ibfk_1` FOREIGN KEY (`id_libreta_fk`) REFERENCES `libreta` (`id_libreta`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimiento`
--

LOCK TABLES `movimiento` WRITE;
/*!40000 ALTER TABLE `movimiento` DISABLE KEYS */;
INSERT INTO `movimiento` VALUES (9,7,'2026-05-18','compra',50.00,'Gasto','SUPERMERCADO',NULL,'Tarjeta','Juan'),(10,7,'2026-05-18','Multa',100.00,'Gasto','IMPUESTOS',NULL,'Tarjeta','Ana'),(14,10,'2026-05-18','bdsbcd',5661156.00,'Ingreso','OTROS','Nómina',NULL,'yo'),(17,11,'2026-05-20','Salon',200.00,'Gasto','OCIO',NULL,'Tarjeta','Javier'),(18,12,'2026-05-21','compra',150.00,'Gasto','SUPERMERCADO',NULL,'Tarjeta','cristina y mama'),(19,13,'2026-05-21','Vuelos',207.00,'Gasto','OTROS',NULL,'Tarjeta','Cristina'),(20,13,'2026-05-01','Sueldo',1500.00,'Ingreso','OTROS','Nómina',NULL,'Ana');
/*!40000 ALTER TABLE `movimiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'test@gmail.com','1234'),(2,'cristina@gmail.com','1234'),(3,'juan@gmail.com','1234'),(4,'ana@gmail.com','1234'),(5,'virginia@gmail.com','1234'),(6,'javier@gmail.com','1234'),(8,'cristina@hormail.com','1234');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_libreta`
--

DROP TABLE IF EXISTS `usuario_libreta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_libreta` (
  `id_usuario` int NOT NULL,
  `id_libreta` int NOT NULL,
  `rol` varchar(50) DEFAULT 'Propietario',
  PRIMARY KEY (`id_usuario`,`id_libreta`),
  KEY `id_libreta` (`id_libreta`),
  CONSTRAINT `usuario_libreta_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE,
  CONSTRAINT `usuario_libreta_ibfk_2` FOREIGN KEY (`id_libreta`) REFERENCES `libreta` (`id_libreta`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_libreta`
--

LOCK TABLES `usuario_libreta` WRITE;
/*!40000 ALTER TABLE `usuario_libreta` DISABLE KEYS */;
INSERT INTO `usuario_libreta` VALUES (1,1,'Propietario'),(2,2,'Propietario'),(2,10,'Propietario'),(2,13,'Propietario'),(2,14,'Propietario'),(3,7,'Propietario'),(3,14,'Invitado'),(4,7,'Propietario'),(4,13,'Invitado'),(4,14,'Invitado'),(5,11,'Invitado'),(6,11,'Propietario'),(8,12,'Propietario');
/*!40000 ALTER TABLE `usuario_libreta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-22 17:50:01
