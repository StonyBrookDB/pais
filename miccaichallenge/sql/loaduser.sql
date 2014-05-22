LOAD FROM "/home/avpuser/production/miccai/TCGA-02-0006-01Z-00-DX1_mask.map" OF DEL MODIFIED BY 
COLDEL,
METHOD P (1, 2, 3, 4) MESSAGES "/tmp/miccai/TCGA-02-0006-01Z-00-DX1_mask.log" 
INSERT INTO 
MICCAI.USERMASK ("USER", IMAGE, X, Y) COPY NO INDEXING MODE AUTOSELECT;
