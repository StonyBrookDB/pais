(=
This script is used to create the database, alter configurations for the database, create bufferpools, and create tablespaces to be used by the database.
The following information needs to be specified:
  Set database name: replace TESTDB as the real database name;
  Set database storage path;
  Set database DBPATH;
  Set database newlogpath;
Database parameters may also need to be adjusted based on server resources.
The script needs to be executed using db2 instance user (e.g., db2inst1).
Run the script as "db2 -tf createdb.sql". 
=)

-- Create database
DROP DATABASE TESTDB;

CREATE DATABASE TESTDB AUTOMATIC STORAGE YES  ON '/db/shared/database/data/testdb' DBPATH ON  '/db/shared/database/data/testdb' USING CODESET ISO-8859-1 TERRITORY US COLLATE USING SYSTEM PAGESIZE 4096;

CONNECT TO TESTDB;

-- Update database configuration
UPDATE DB CFG FOR TESTDB USING AUTO_MAINT ON;
UPDATE DB CFG FOR TESTDB USING AUTO_TBL_MAINT ON;
UPDATE DB CFG FOR TESTDB USING AUTO_RUNSTATS ON;
UPDATE DB CFG FOR TESTDB USING AUTO_REORG ON;
UPDATE DB CFG FOR TESTDB USING AUTO_DB_BACKUP ON;
UPDATE DB CFG FOR TESTDB USING logarchmeth1 OFF logarchmeth2 OFF logprimary 125 logsecond 125 logfilsiz 4096 newlogpath /db/shared/database/log/testdb;

UPDATE DATABASE CONFIGURATION USING APPLHEAPSZ 20484 AUTOMATIC IMMEDIATE ;
UPDATE DATABASE CONFIGURATION USING DBHEAP 25500 AUTOMATIC IMMEDIATE ;
UPDATE DATABASE CONFIGURATION USING STMTHEAP 16384 AUTOMATIC IMMEDIATE ;
UPDATE DATABASE CONFIGURATION USING LOGBUFSZ 2560 DEFERRED ;

UPDATE ALERT CFG FOR DATABASE ON TESTDB USING db.db_backup_req SET THRESHOLDSCHECKED YES;
UPDATE ALERT CFG FOR DATABASE ON TESTDB USING db.tb_reorg_req SET THRESHOLDSCHECKED YES;
UPDATE ALERT CFG FOR DATABASE ON TESTDB USING db.tb_runstats_req SET THRESHOLDSCHECKED YES;

--before creating buffer pools, database partition group must be defined
create database partition group pg30 on dbpartitionnums(0 to 29);
create database partition group pg1 on dbpartitionnums(0 to 0);

-- Create buffer pools
CREATE BUFFERPOOL BP32K IMMEDIATE DATABASE PARTITION GROUP pg30 SIZE 10000 PAGESIZE 32 K ;
CREATE BUFFERPOOL BPL16K IMMEDIATE DATABASE PARTITION GROUP pg30 SIZE 10000 AUTOMATIC PAGESIZE 16 K ;

CREATE BUFFERPOOL BPPG1 IMMEDIATE DATABASE PARTITION GROUP pg1 SIZE 10000 PAGESIZE 32 K ;
CREATE BUFFERPOOL BP32K2 IMMEDIATE DATABASE PARTITION GROUP IBMTEMPGROUP SIZE 10000 PAGESIZE 32 K ;


-- Create tablespaces

--space for master node
CREATE TABLESPACE master IN DATABASE PARTITION GROUP pg1 PAGESIZE 32 K MANAGED BY DATABASE USING  (file '/db/shared/database/data/testdb/master' 20G) 
EXTENTSIZE 256 PREFETCHSIZE 256  BUFFERPOOL BPPG1;


--spatial tablespace
CREATE LARGE TABLESPACE SPATIALTBS32K IN DATABASE PARTITION GROUP pg30 PAGESIZE 32 K MANAGED BY DATABASE USING  (file '/db/shared/database/data/testdb $N' 50G) 
EXTENTSIZE 60 OVERHEAD 10.67 PREFETCHSIZE 60 TRANSFERRATE 0.04 BUFFERPOOL  BP32K ;

--temporary tablespace
CREATE  SYSTEM TEMPORARY  TABLESPACE TMPTBS32K PAGESIZE 32 K  MANAGED BY AUTOMATIC STORAGE 
EXTENTSIZE 60 OVERHEAD 10.67 PREFETCHSIZE 60 TRANSFERRATE 0.04 BUFFERPOOL  BP32K2 ;

