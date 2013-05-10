-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 10, 2013 at 09:10 PM
-- Server version: 5.5.29
-- PHP Version: 5.3.10-1ubuntu3.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
DROP DATABASE IF EXISTS idp;
CREATE DATABASE idp;
USE idp;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `idp`
--

-- --------------------------------------------------------

--
-- Table structure for table `Service`
--

CREATE TABLE IF NOT EXISTS `Service` (
  `ID` int(3) NOT NULL AUTO_INCREMENT,
  `ServiceName` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ServiceName` (`ServiceName`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `ID` int(3) NOT NULL AUTO_INCREMENT,
  `Name` varchar(24) NOT NULL,
  `Type` int(3) NOT NULL,
  `Status` int(1) NOT NULL,
  `Password` varchar(30) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`),
  KEY `Type` (`Type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `UserToService`
--

CREATE TABLE IF NOT EXISTS `UserToService` (
  `ID` int(3) NOT NULL AUTO_INCREMENT,
  `UserID` int(3) NOT NULL,
  `ServiceID` int(3) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `UserID` (`UserID`),
  KEY `ServiceID` (`ServiceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `UserType`
--

CREATE TABLE IF NOT EXISTS `UserType` (
  `ID` int(3) NOT NULL AUTO_INCREMENT,
  `TypeName` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TypeName` (`TypeName`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `UserType`
--

INSERT INTO `UserType` (`ID`, `TypeName`) VALUES
(2, 'BUYER'),
(1, 'MANUFACTURER');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `User`
--
ALTER TABLE `User`
  ADD CONSTRAINT `User_ibfk_1` FOREIGN KEY (`Type`) REFERENCES `UserType` (`ID`);

--
-- Constraints for table `UserToService`
--
ALTER TABLE `UserToService`
  ADD CONSTRAINT `UserToService_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`),
  ADD CONSTRAINT `UserToService_ibfk_2` FOREIGN KEY (`ServiceID`) REFERENCES `Service` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
