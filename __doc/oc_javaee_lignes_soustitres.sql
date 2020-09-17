CREATE DATABASE  IF NOT EXISTS `oc_javaee` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `oc_javaee`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: oc_javaee
-- ------------------------------------------------------

 SET NAMES utf8 ;

--
-- Table structure for table `lignes_soustitres`
--

DROP TABLE IF EXISTS `lignes_soustitres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lignes_soustitres` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numero` int(11) NOT NULL,
  `position_temps` varchar(45) NOT NULL,
  `texte` varchar(255) NOT NULL,
  `texte_original` varchar(255) NOT NULL,
  `nbr_lignes` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_UNIQUE` (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Dump completed on 2019-04-13 12:00:49
