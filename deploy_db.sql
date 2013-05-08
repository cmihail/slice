-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 08, 2013 at 01:53 PM
-- Server version: 5.5.29
-- PHP Version: 5.3.10-1ubuntu3.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `Service`
--

INSERT INTO `Service` (`ID`, `ServiceName`) VALUES
(1, 'service1'),
(2, 'service2'),
(3, 'service3'),
(4, 'service4');

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`ID`, `Name`, `Type`, `Status`, `Password`) VALUES
(1, 'user1', 1, 0, 'user1'),
(2, 'user2', 2, 0, 'user2'),
(3, 'user3', 1, 0, 'user3'),
(4, 'user4', 2, 0, 'user4');

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `UserToService`
--

INSERT INTO `UserToService` (`ID`, `UserID`, `ServiceID`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 2, 1),
(6, 2, 2),
(7, 2, 3),
(8, 2, 4),
(9, 3, 1),
(10, 3, 2),
(11, 3, 3),
(12, 3, 4),
(13, 4, 1),
(14, 4, 2),
(15, 4, 3),
(16, 4, 4);

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
(1, 'MMANUFACTURER');

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
  ADD CONSTRAINT `UserToService_ibfk_2` FOREIGN KEY (`ServiceID`) REFERENCES `Service` (`ID`),
  ADD CONSTRAINT `UserToService_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
