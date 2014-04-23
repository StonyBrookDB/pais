drop table db2gse.gse_srs_replicated;
-- MQT to replicate the SRS information across all nodes in a DPF environment
CREATE TABLE db2gse.gse_srs_replicated AS
   ( SELECT srs_name, srs_id, x_offset, x_scale, y_offset, z_offset, z_scale, m_offset, m_scale, definition
		      FROM   db2gse.gse_spatial_reference_systems )
      	DATA INITIALLY DEFERRED
      	REFRESH IMMEDIATE
	ENABLE QUERY OPTIMIZATION
       	REPLICATED
	IN SPATIALTBS32K
;

REFRESH TABLE db2gse.gse_srs_replicated;

CREATE  INDEX db2gse.gse_srs_id_replicated ON db2gse.gse_srs_replicated ( srs_id )
;

SET INTEGRITY FOR db2gse.gse_srs_replicated ALL IMMEDIATE UNCHECKED;

RUNSTATS ON TABLE db2gse.gse_srs_replicated and indexes all;
