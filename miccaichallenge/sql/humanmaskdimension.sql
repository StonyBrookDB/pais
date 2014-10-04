DROP TABLE MICCAI.humanmaskdimension;

CREATE TABLE MICCAI.humanmaskdimension(
 image VARCHAR(32) NOT NULL,
 x INT NOT NULL,
 y INT NOT NULL,
 NECROSIS INT NOT NULL,
 PRIMARY KEY (image)
)
COMPRESS YES
IN SPATIALTBS32K;


LOAD FROM "humanmaskdimension.txt"  OF DEL MODIFIED BY COLDEL, 
METHOD P (1, 2, 3, 4) MESSAGES "humanmaskdimensionload.log"
INSERT INTO MICCAI.humanmaskdimension (image,x,y,necrosis) COPY NO INDEXING MODE AUTOSELECT;
