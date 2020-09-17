# Génération base de données et table 

Génération base de données "oc_javaee" et table "lignes_soustitres"

solution 1
copier le code et générer la requête sql

solution 2
importer le fichier oc_javaee_lignes_soustitres.sql

````sql
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
````

## Renseigner les bons paramètres de connexion BDD dans DaoFactory

Mes paramètres de login sont user: "root" et password : "root"
ils sont définis dans DaoFactory lignes 9 et 10

Renseignez vos paramètres :

````java
package com.subtitlor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
	private static final String HOST_DB_NAME = "jdbc:mysql://localhost:3306/oc_javaee?serverTimezone=UTC";
	private static final String USER_LOGIN = "root";
	private static final String PASSWORD = "root";
````

## fichiers de l'application

Classes Interfaces et Servlets

packages com.subtitlor.

	.beans
		+ BeanException.java
		+ LigneSoustitre.java
	.dao
		+ DaoException.java
		+ DaoFactory.java
		+ LigneSoustitreDao.java
		+ LigneSoustitreDaoImpl.java
	.servlets
		+ Accueil.java
		+ EditSubtitle.java
		+ UploadFichier.java
	.utilities
		+ SubtitlesHandler.java
		+ SubtitlesHandlerException.java
		
JSP et fichiers de configuration

	/WebContent
		+ index.jsp
	/WebContent/WEB-INF
		+ edit_subtitle.jsp
		+ taglibs.jsp
		+ uploadfichier_a_traduire_srt.jsp
		. web.xml
		
Librairies (sans projet maven) à télécharger dans le répertoire /lib

	/WebContent/lib
		+ jstl-1.2.jar
		+ mysql-connector-java-8.0.15.jar
ou

Librairies projet maven ; ajouter les dépendances ci-dessous

````xml
  <dependencies>
    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.15</version>
    </dependency>
  </dependencies>
````
	

## problème d'encodage jsp lors du post - web.xml

[encode jsp utf8 post](https://openclassrooms.com/fr/courses/626954-creez-votre-application-web-avec-java-ee/622893-le-filtre-creez-un-espace-membre)

ajouter web.xml un filtre d'encodage

````xml
	<filter>
	    <filter-name>Set Character Encoding</filter-name>
	    <filter-class>org.apache.catalina.filters.SetCharacterEncodingFilter</filter-class>
	    <init-param>
	        <param-name>encoding</param-name>
	        <param-value>UTF-8</param-value>
	    </init-param>
	    <init-param>
	        <param-name>ignore</param-name>
	        <param-value>false</param-value>
	    </init-param>
	</filter>
	<filter-mapping>
	    <filter-name>Set Character Encoding</filter-name>
	    <url-pattern>/*</url-pattern>
	</filter-mapping>
````

## Resultset - récupérer le nombre de résultats

[Getting the number of rows returned by resultset](https://www.experts-exchange.com/questions/21408112/Getting-the-number-of-rows-returned-by-resultset-getFetchSize.html)

Code explaination as follows

````java
// Initialize a variable
int count = 0;

//Move the resultset to Last row
rs.last();

//Get the current row number (The first row is number 1, the second number 2, and so on)
count = rs.getRow(); // 0 if there is no current row

//Again move the resultset pointer to first row for further operation with ur resultset
rs.beforeFirst();
````

## créer répertoire et dossier avec java

[créer un repertoire s'il nexiste pas](http://java.mesexemples.com/fichiersrepertoires/java-creer-un-repertoire-sil-nexiste-pas/)

[creer un fichier texte avec createnewfile](https://codes-sources.commentcamarche.net/forum/affich-136644-creer-un-fichier-texte-avec-createnewfile)

[how to create a new file](https://howtodoinjava.com/java/io/how-to-create-a-new-file-in-java/)


