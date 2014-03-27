--<ScriptOptions statementTerminator=";"/>
DROP TABLE PI.DATASET ;
DROP TABLE PI.IMAGE ;
DROP TABLE PI.TILEDIMAGE;
DROP TABLE PI.SERVER;
DROP TABLE PI.LOCATION;
DROP TABLE PI.PROJECT ;
DROP TABLE PI.EXPERIMENTER;
DROP TABLE PI.EXPERIMENTERGROUP;
DROP TABLE PI.ACTIVITYSTATUS ;
DROP TABLE PI.PATIENT ;
DROP TABLE PI.SPECIMEN;
DROP TABLE PI.INSTRUMENT;
DROP TABLE PI.EXPERIMENT;
DROP TABLE PI.EXPERIMENTALSTUDY;
DROP TABLE PI.TILESET;
DROP TABLE PI.EXPERIMENTERGROUPEXPERIMENTER;
DROP TABLE PI.ACTIVITYSTATUSIMAGE;

--DROP SCHEMA PI;
--CREATE SCHEMA PI;

CREATE TABLE PI.DATASET (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		DESCRIPTION VARCHAR(255),
		DATASET_NAME VARCHAR(255),
		IMAGE_FORMAT BIGINT,
		PERMISSIONS BIGINT,
		NAME VARCHAR(255),
		VERSION INTEGER,
		CREATION_ID BIGINT,
		EXTERNAL_ID BIGINT,
		GROUP_ID BIGINT,
		OWNER_ID BIGINT,
		UPDATE_ID BIGINT
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.IMAGE (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		ACQUISITIONDATE TIMESTAMP,
		ARCHIVED CHAR(1),
		DESCRIPTION VARCHAR(255),
		PERMISSIONS BIGINT,
		NAME VARCHAR(255),
		PARTIAL CHAR(1),
		VERSION INTEGER,
		EXPERIMENT BIGINT,
		FORMAT BIGINT,
		IMAGINGENVIRONMENT BIGINT,
		OBJECTIVESETTINGS BIGINT,
		STAGELEBEL BIGINT,
		CREATION_ID BIGINT,
		EXTERNAL_ID BIGINT,
		GROUP_ID BIGINT,
		OWNER_ID BIGINT,
		UPDATE_ID BIGINT,
		FILESIZE BIGINT,
		RESOLUTION VARCHAR(64),
		IMAGEREFERENCE_UID VARCHAR(64),
		INSTRUMENT BIGINT,
		THUMBNAIL BLOB(1024000),
		BIG_THUMBNAIL BLOB(1024000),
		PATIENT_ID BIGINT,
		DATASET_ID BIGINT,
		SPECIMEN_ID BIGINT,
		LOCATION_ID BIGINT,
		WIDTH BIGINT,
		HEIGHT BIGINT
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.TILEDIMAGE (
		NAME VARCHAR(255),
		X DOUBLE,
		Y DOUBLE,
		HEIGHT DOUBLE,
		FILESIZE BIGINT,
		WIDTH DOUBLE,
		RESOLUTION VARCHAR(64),
		TILEDTIME TIMESTAMP,
		FORMAT BIGINT,
		HIERARCHYLEVEL CHAR(1),
		LOCATION_ID BIGINT,
		THUMBNAIL BLOB(102400000),
		TILENAME VARCHAR(255),
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		IMAGE_ID BIGINT NOT NULL,
		TILESET_ID BIGINT NOT NULL
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.SERVER (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		NAME VARCHAR(255),
		CAPACITY VARCHAR(64),
		HOSTNAME VARCHAR(64),
		IPADDRESS VARCHAR(64),
		PORT INTEGER
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.LOCATION (
		FOLDER VARCHAR(255),
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		SERVER_ID BIGINT NOT NULL
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.PROJECT (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		DESCRIPTION VARCHAR(255),
		PERMISSIONS BIGINT,
		NAME VARCHAR(255),
		VERSION INTEGER,
		CREATION_ID BIGINT,
		EXTERNAL_ID BIGINT,
		GROUP_ID BIGINT,
		OWNER_ID BIGINT,
		UPDATE_ID BIGINT,
		PROJECT_UID VARCHAR(64),
		URI VARCHAR(64)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.EXPERIMENTER (
		PERMISSIONS BIGINT,
		EMAIL VARCHAR(255),
		FIRSTNAME VARCHAR(255),
		INSTITUTION VARCHAR(255),
		LASTNAME VARCHAR(255),
		MIDDLENAME VARCHAR(255),
		OMENAME VARCHAR(255),
		VERSION INTEGER,
		EXTERNAL_ID BIGINT,
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		USER_UID VARCHAR(64),
		LOGINNAME VARCHAR(64),
		NAME VARCHAR(64)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.EXPERIMENTERGROUP (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		DESCRIPTION VARCHAR(255),
		PERMISSIONS VARCHAR(255),
		NAME VARCHAR(255),
		VERSION INTEGER,
		EXTERNAL_ID BIGINT,
		GROUP_UID VARCHAR(64),
		URI VARCHAR(64)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.ACTIVITYSTATUS (
		STATUS VARCHAR(64),
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		DESCRIPTION VARCHAR(255)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.PATIENT (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		NAME VARCHAR(64),
		SEX VARCHAR(64),
		BIRTHDATE VARCHAR(64),
		ETHNICGROUP VARCHAR(64),
		PATIENTID VARCHAR(64)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.SPECIMEN (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		TYPE VARCHAR(64),
		STAIN VARCHAR(64),
		SPECIMEN_UID VARCHAR(64),
		SLICE_ID VARCHAR(64),
		TISSUE_ID VARCHAR(64)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.INSTRUMENT (
		PERMISSIONS BIGINT,
		VERSION INTEGER,
		CREATION_ID BIGINT,
		EXTERNAL_ID BIGINT,
		GROUP_ID BIGINT,
		OWNER_ID BIGINT,
		UPDATE_ID BIGINT,
		MICROSCOPE BIGINT,
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		MANUFACTURER VARCHAR(255),
		MANUFACTURERMODELNAME VARCHAR(255),
		SOFTWAREVERSION VARCHAR(64)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.EXPERIMENT (
		DESCRIPTION VARCHAR(255),
		PERMISSIONS BIGINT,
		VERSION INTEGER,
		CREATION_ID BIGINT,
		EXTERNAL_ID BIGINT,
		GROUP_ID BIGINT,
		UPDATE_ID BIGINT,
		OWNER_ID BIGINT,
		TYPE BIGINT,
		ESTUDY_ID BIGINT,
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.EXPERIMENTALSTUDY (
		ACTIVEDATERANGE DATE,
		DESCRIPTION VARCHAR(255),
		DESIGNTYPE VARCHAR(255),
		NAME VARCHAR(200),
		TYPECODE VARCHAR(200),
		EXPERIMENTER_ID BIGINT,
		PROJECT_ID BIGINT,
		DATASET_ID BIGINT,
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.TILESET (
		ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20),
		NAME VARCHAR(255),
		TYPE VARCHAR(64),
		DESCRIPTION VARCHAR(255)
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.EXPERIMENTERGROUPEXPERIMENTER (
		ID BIGINT NOT NULL,
		ID1 BIGINT NOT NULL
	)
	DATA CAPTURE NONE;

CREATE TABLE PI.ACTIVITYSTATUSIMAGE (
		ID BIGINT NOT NULL,
		ID1 BIGINT NOT NULL
	)
	DATA CAPTURE NONE;

ALTER TABLE PI.DATASET ADD CONSTRAINT DATASET_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.IMAGE ADD CONSTRAINT IMAGE_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.TILEDIMAGE ADD CONSTRAINT TILEDIMAGE_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.SERVER ADD CONSTRAINT SERVER_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.LOCATION ADD CONSTRAINT LOCATION_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.PROJECT ADD CONSTRAINT PROJECT_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.EXPERIMENTER ADD CONSTRAINT EXPERIMENTER_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.EXPERIMENTERGROUP ADD CONSTRAINT EXPERIMENTERGROUP_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.ACTIVITYSTATUS ADD CONSTRAINT ACTIVITYSTATUS_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.PATIENT ADD CONSTRAINT PATIENT_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.SPECIMEN ADD CONSTRAINT SPECIMEN_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.INSTRUMENT ADD CONSTRAINT INSTRUMENT_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.EXPERIMENT ADD CONSTRAINT EXPERIMENT_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.EXPERIMENTALSTUDY ADD CONSTRAINT EXPERIMENTALSTUDY_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.TILESET ADD CONSTRAINT TILESET_PK PRIMARY KEY
	(ID);

ALTER TABLE PI.EXPERIMENTERGROUPEXPERIMENTER ADD CONSTRAINT EXPERIMENTERGROUPEXPERIMENTER_PK PRIMARY KEY
	(ID,
	 ID1);

ALTER TABLE PI.ACTIVITYSTATUSIMAGE ADD CONSTRAINT ACTIVITYSTATUSIMAGE_PK PRIMARY KEY
	(ID,
	 ID1);

ALTER TABLE PI.IMAGE ADD CONSTRAINT IMAGE_INSTRUMENT_FK FOREIGN KEY
	(INSTRUMENT)
	REFERENCES PI.INSTRUMENT
	(ID);

ALTER TABLE PI.IMAGE ADD CONSTRAINT IMAGE_PATIENT_FK FOREIGN KEY
	(PATIENT_ID)
	REFERENCES PI.PATIENT
	(ID);

ALTER TABLE PI.IMAGE ADD CONSTRAINT IMAGE_DATASET_FK FOREIGN KEY
	(DATASET_ID)
	REFERENCES PI.DATASET
	(ID);

ALTER TABLE PI.IMAGE ADD CONSTRAINT IMAGE_SPECIMEN_FK FOREIGN KEY
	(SPECIMEN_ID)
	REFERENCES PI.SPECIMEN
	(ID);

ALTER TABLE PI.IMAGE ADD CONSTRAINT IMAGE_LOCATION_FK FOREIGN KEY
	(LOCATION_ID)
	REFERENCES PI.LOCATION
	(ID);

ALTER TABLE PI.TILEDIMAGE ADD CONSTRAINT TILEDIMAGE_LOCATION_FK FOREIGN KEY
	(LOCATION_ID)
	REFERENCES PI.LOCATION
	(ID);

ALTER TABLE PI.TILEDIMAGE ADD CONSTRAINT TILEDIMAGE_IMAGE_FK FOREIGN KEY
	(IMAGE_ID)
	REFERENCES PI.IMAGE
	(ID);

ALTER TABLE PI.TILEDIMAGE ADD CONSTRAINT TILEDIMAGE_TILESET_FK FOREIGN KEY
	(TILESET_ID)
	REFERENCES PI.TILESET
	(ID);

ALTER TABLE PI.LOCATION ADD CONSTRAINT LOCATION_SERVER_FK FOREIGN KEY
	(SERVER_ID)
	REFERENCES PI.SERVER
	(ID);

ALTER TABLE PI.EXPERIMENT ADD CONSTRAINT EXPERIMENT_EXPERIMENTALSTUDY_FK FOREIGN KEY
	(ESTUDY_ID)
	REFERENCES PI.EXPERIMENTALSTUDY
	(ID);

ALTER TABLE PI.EXPERIMENTALSTUDY ADD CONSTRAINT EXPERIMENTALSTUDY_EXPERIMENTER_FK FOREIGN KEY
	(EXPERIMENTER_ID)
	REFERENCES PI.EXPERIMENTER
	(ID);

ALTER TABLE PI.EXPERIMENTALSTUDY ADD CONSTRAINT EXPERIMENTALSTUDY_PROJECT_FK FOREIGN KEY
	(PROJECT_ID)
	REFERENCES PI.PROJECT
	(ID);

ALTER TABLE PI.EXPERIMENTALSTUDY ADD CONSTRAINT EXPERIMENTALSTUDY_DATASET_FK FOREIGN KEY
	(DATASET_ID)
	REFERENCES PI.DATASET
	(ID);

ALTER TABLE PI.EXPERIMENTERGROUPEXPERIMENTER ADD CONSTRAINT EXPERIMENTERGROUPEXPERIMENTER_EXPERIMENTERGROUP_FK FOREIGN KEY
	(ID)
	REFERENCES PI.EXPERIMENTERGROUP
	(ID);

ALTER TABLE PI.EXPERIMENTERGROUPEXPERIMENTER ADD CONSTRAINT EXPERIMENTERGROUPEXPERIMENTER_EXPERIMENTER_FK FOREIGN KEY
	(ID1)
	REFERENCES PI.EXPERIMENTER
	(ID);

ALTER TABLE PI.ACTIVITYSTATUSIMAGE ADD CONSTRAINT ACTIVITYSTATUSIMAGE_ACTIVITYSTATUS_FK FOREIGN KEY
	(ID)
	REFERENCES PI.ACTIVITYSTATUS
	(ID);

ALTER TABLE PI.ACTIVITYSTATUSIMAGE ADD CONSTRAINT ACTIVITYSTATUSIMAGE_IMAGE_FK FOREIGN KEY
	(ID1)
	REFERENCES PI.IMAGE
	(ID);

