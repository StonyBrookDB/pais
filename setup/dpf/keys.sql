-- Scripts to recreate primary keys

-- Job tables
ALTER TABLE PAIS.STAGINGDOC 	ADD PRIMARY KEY(SEQUENCE_NUMBER);
ALTER TABLE PAIS.METADOC 	ADD PRIMARY KEY(UID);

-- Metadata tables
ALTER TABLE PAIS.PAIS 		ADD PRIMARY KEY(PAIS_UID);
ALTER TABLE PAIS.PROJECT	ADD PRIMARY KEY(PROJECT_ID, PAIS_UID);
ALTER TABLE PAIS.GROUP		ADD PRIMARY KEY(GROUP_ID, PAIS_UID);
ALTER TABLE PAIS.USER		ADD PRIMARY KEY(USER_ID, PAIS_UID);
ALTER TABLE PAIS.COLLECTION	ADD PRIMARY KEY(PAIS_UID, COLLECTION_UID, METHODNAME, SEQUENCENUMBER);
ALTER TABLE PAIS.WHOLESLIDEIMAGEREFERENCE ADD PRIMARY KEY(PAIS_UID, IMAGEREFERENCE_ID);

ALTER TABLE PAIS.REGION		ADD PRIMARY KEY(PAIS_UID, NAME);
ALTER TABLE PAIS.SPECIMEN	ADD PRIMARY KEY(PAIS_UID, SPECIMEN_ID);
ALTER TABLE PAIS.ANATOMICENTITY ADD PRIMARY KEY(PAIS_UID, ANATOMICENTITY_ID);
ALTER TABLE PAIS.PATIENT 	ADD PRIMARY KEY(PAIS_UID, PATIENT_ID);
ALTER TABLE PAIS.CALCULATION_DICTIONARY ADD PRIMARY KEY(DICTIONARY_ID);
ALTER TABLE PAIS.OBSERVATION_DICTIONARY ADD PRIMARY KEY(DICTIONARY_ID);

-- Instance tables
ALTER TABLE PAIS.CALCULATION_FLAT  ADD PRIMARY KEY(PAIS_UID, TILENAME, MARKUP_ID);

-- Duplicate records, changed for current
--ALTER TABLE PAIS.OBSERVATION_QUANTIFICATION_ORDINAL ADD PRIMARY KEY(PAIS_UID, TILENAME, OBSERVATION_ID);
ALTER TABLE PAIS.OBSERVATION_QUANTIFICATION_ORDINAL ADD PRIMARY KEY(PAIS_UID, OBSERVATION_ID);

--ALTER TABLE PAIS.OBSERVATION_QUANTIFICATION_NOMINAL ADD PRIMARY KEY(PAIS_UID, TILENAME, OBSERVATION_ID);
ALTER TABLE PAIS.OBSERVATION_QUANTIFICATION_NOMINAL ADD PRIMARY KEY(PAIS_UID, OBSERVATION_ID);


ALTER TABLE PAIS.MARKUP_POLYGON	 	ADD PRIMARY KEY(PAIS_UID, MARKUP_ID);
ALTER TABLE PAIS.MARKUP_POLYGON_HUMAN 	ADD PRIMARY KEY(PAIS_UID, MARKUP_ID);

-- Provenance tables
ALTER TABLE PAIS.PROVENANCE	ADD PRIMARY KEY(PAIS_UID, PROVENANCE_ID);
ALTER TABLE PAIS.ALGORITHM 	ADD PRIMARY KEY(PAIS_UID, ALGORITHM_ID);
ALTER TABLE PAIS.PARAMETER 	ADD PRIMARY KEY(PAIS_UID, PROVENANCE_ID, PARAMETER_ID);
ALTER TABLE PAIS.INPUTFILEREFERENCE 	ADD PRIMARY KEY(PAIS_UID, PROVENANCE_ID, INPUTFILEREFERENCE_ID);


