-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 16, 2025 at 08:06 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `todolist`
--

-- --------------------------------------------------------

--
-- Table structure for table `tasks`
--

CREATE TABLE `tasks` (
  `no` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  `dueDate` date NOT NULL,
  `category` varchar(20) NOT NULL,
  `priority` varchar(20) NOT NULL,
  `completion` varchar(20) NOT NULL,
  `recurrence` varchar(20) DEFAULT NULL,
  `dependencies` int(11) DEFAULT NULL,
  `rccDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tasks`
--

INSERT INTO `tasks` (`no`, `title`, `description`, `dueDate`, `category`, `priority`, `completion`, `recurrence`, `dependencies`, `rccDate`) VALUES
(4, 'Clean the house', 'Make sure every corner is clean', '2025-01-23', 'Personal', 'Low', 'Complete', 'None', 5, NULL),
(5, 'Reply to emails', 'Respond to work-related emails', '2025-01-24', 'Personal', 'Medium', 'Complete', 'None', 6, NULL),
(6, 'Call mom', 'Check in with mom and catch up', '2025-01-25', 'Homework', 'Medium', 'Complete', 'None', 5, NULL),
(7, 'Walk the dog', 'Take the dog for a 30-minute walk', '2025-01-26', 'Homework', 'Medium', 'Incomplete', 'None', NULL, NULL),
(8, 'Update website', 'Add new blog post to the company website', '2025-01-27', 'Homework', 'Medium', 'Incomplete', 'None', 10, NULL),
(9, 'Study for exam', 'Review notes for the upcoming exam', '2025-01-28', 'Homework', 'Medium', 'Incomplete', 'None', NULL, NULL),
(10, 'Prepare dinner', 'Cook pasta and salad for dinner', '2025-01-29', 'Personal', 'High', 'Complete', 'None', NULL, NULL),
(11, 'Pay bills', 'Pay the electricity and internet bills online', '2025-01-30', 'Work', 'High', 'Complete', 'None', 6, NULL),
(12, 'Read a book', 'Finish the next chapter of the book', '2025-01-31', 'Work', 'High', 'Complete', 'None', 1, NULL),
(13, 'Plan weekend trip', 'Research destinations for a short weekend getaway', '2025-01-24', 'Work', 'High', 'Complete', 'None', NULL, NULL),
(14, 'Backup data', 'Backup important files to an external drive', '2025-01-22', 'Personal', 'Low', 'Incomplete', 'None', 19, NULL),
(19, 'Finish Something', 'I like you', '2025-01-31', 'Personal', 'Medium', 'Incomplete', 'None', NULL, NULL),
(24, 'Presentation', 'fop', '2025-01-19', 'Work', 'Medium', 'Incomplete', 'None', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`no`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tasks`
--
ALTER TABLE `tasks`
  MODIFY `no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
