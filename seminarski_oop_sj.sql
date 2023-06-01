-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3307
-- Generation Time: Nov 24, 2022 at 12:40 AM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ors1_oop_2022_2023`
--

-- --------------------------------------------------------

--
-- Table structure for table `izvodjenje_predstave`
--

CREATE TABLE `izvodjenje_predstave` (
  `id` int(11) NOT NULL,
  `predstava_id` int(11) NOT NULL,
  `pozoriste_id` int(11) NOT NULL,
  `cijena` decimal(10,0) NOT NULL,
  `datum_i_vrijeme` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `karta`
--

CREATE TABLE `karta` (
  `id` int(11) NOT NULL,
  `izvodjenje_predstave_id` int(11) NOT NULL,
  `status` int(3) NOT NULL,
  `posjetilac_id` int(11) DEFAULT NULL,
  `broj_karta` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `osoblje`
--

CREATE TABLE `osoblje` (
  `id` int(11) NOT NULL,
  `ime` varchar(45) NOT NULL,
  `prezime` varchar(45) NOT NULL,
  `tip` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `osoblje_predstave`
--

CREATE TABLE `osoblje_predstave` (
  `id` int(11) NOT NULL,
  `osoblje_id` int(11) NOT NULL,
  `predstava_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `posjetilac_pozorista`
--

CREATE TABLE `posjetilac_pozorista` (
  `id` int(11) NOT NULL,
  `ime` varchar(45) NOT NULL,
  `prezime` varchar(45) NOT NULL,
  `korisincko_ime` varchar(45) NOT NULL,
  `lozinka` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `pozoriste`
--

CREATE TABLE `pozoriste` (
  `id` int(11) NOT NULL,
  `naziv` varchar(45) NOT NULL,
  `grad` varchar(45) NOT NULL,
  `broj_sjedista` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `predstava`
--

CREATE TABLE `predstava` (
  `id` int(11) NOT NULL,
  `naziv` varchar(45) NOT NULL,
  `zanr` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `radnik_pozorista`
--

CREATE TABLE `radnik_pozorista` (
  `id` int(11) NOT NULL,
  `ime` varchar(45) NOT NULL,
  `prezime` varchar(45) NOT NULL,
  `korisnicko_ime` varchar(45) NOT NULL,
  `lozinka` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `izvodjenje_predstave`
--
ALTER TABLE `izvodjenje_predstave`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `karta`
--
ALTER TABLE `karta`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `osoblje`
--
ALTER TABLE `osoblje`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `osoblje_predstave`
--
ALTER TABLE `osoblje_predstave`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `posjetilac_pozorista`
--
ALTER TABLE `posjetilac_pozorista`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pozoriste`
--
ALTER TABLE `pozoriste`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `predstava`
--
ALTER TABLE `predstava`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `radnik_pozorista`
--
ALTER TABLE `radnik_pozorista`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `izvodjenje_predstave`
--
ALTER TABLE `izvodjenje_predstave`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `karta`
--
ALTER TABLE `karta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `osoblje`
--
ALTER TABLE `osoblje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `osoblje_predstave`
--
ALTER TABLE `osoblje_predstave`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `posjetilac_pozorista`
--
ALTER TABLE `posjetilac_pozorista`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pozoriste`
--
ALTER TABLE `pozoriste`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `predstava`
--
ALTER TABLE `predstava`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `radnik_pozorista`
--
ALTER TABLE `radnik_pozorista`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;