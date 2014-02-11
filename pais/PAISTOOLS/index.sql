
CREATE INDEX PAIS.CAL_MID ON PAIS.CALCULATION_FLAT(PAIS_UID, MARKUP_ID);

--CREATE INDEX PAIS.obs_nom_name  ON  pais.observation_quantification_nominal(OBSERVATION_NAME);
--CREATE INDEX PAIS.obs_nom_quant_value ON  pais.observation_quantification_nominal(quantification_value);

CREATE INDEX PAIS.obs_nom_name  ON  pais.observation_quantification_nominal(OBSERVATION_NAME, quantification_value);
CREATE INDEX PAIS.obs_nom_mid  ON  pais.observation_quantification_nominal(PAIS_UID, MARKUP_ID);

--CREATE INDEX PAIS.obs_ord_name  ON  pais.observation_quantification_ordinal(OBSERVATION_NAME);
--CREATE INDEX PAIS.obs_ord_quant_value ON  pais.observation_quantification_ordinal(quantification_value);

CREATE INDEX PAIS.obs_ord_name  ON  pais.observation_quantification_ordinal(OBSERVATION_NAME, quantification_value);
CREATE INDEX PAIS.obs_ord_mid  ON  pais.observation_quantification_ordinal(PAIS_UID, MARKUP_ID);

RUNSTATS ON TABLE PAIS.CALCULATION_FLAT AND INDEXES ALL;
RUNSTATS ON TABLE pais.observation_quantification_nominal AND INDEXES ALL;
RUNSTATS ON TABLE pais.observation_quantification_ordinal AND INDEXES ALL;
RUNSTATS ON TABLE pais.region AND INDEXES ALL;
RUNSTATS ON TABLE pais.collection AND INDEXES ALL;
RUNSTATS ON TABLE pais.patient AND INDEXES ALL;

RUNSTATS ON TABLE pais.provenance AND INDEXES ALL;
RUNSTATS ON TABLE pais.algorithm AND INDEXES ALL;  
RUNSTATS ON TABLE pais.parameter AND INDEXES ALL; 

COMMIT WORK;

--NEWINDEXES

COMMIT WORK;

 
