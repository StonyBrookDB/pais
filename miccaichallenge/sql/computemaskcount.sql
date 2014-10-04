DROP TABLE miccai.humanmaskcount;

DROP TABLE miccai.usermaskcount;

CREATE TABLE miccai.humanmaskcount(
 image VARCHAR(32) NOT NULL,
 count INT NOT NULL
 );

CREATE TABLE miccai.usermaskcount(
 user VARCHAR(16) NOT NULL,
 image VARCHAR(32) NOT NULL,
 count INT NOT NULL
 );
 

INSERT INTO miccai.humanmaskcount
SELECT image, COUNT(*)
FROM MICCAI.HUMANMASK
GROUP BY image;

INSERT INTO miccai.usermaskcount
SELECT m.user, m.image, COUNT(*)
FROM MICCAI.USERMASK m
GROUP BY m.user, m.image;

