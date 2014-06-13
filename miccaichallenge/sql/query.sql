(-
SELECT count(*) FROM
MICCAI.HUMANMASK a, MICCAI.USERMASK b
WHERE  b.user ='user100' AND
      a.image = 'TCGA-02-0006-01Z-00-DX1' AND
      b.image = 'TCGA-02-0006-01Z-00-DX1' AND
      a.x = b.x AND
      a.y = b.y;
      
      
SELECT COUNT(*) FROM
MICCAI.HUMANMASK a
WHERE image = 'TCGA-02-0006-01Z-00-DX1';



-- Do basic count of masks for each image.
SELECT image, COUNT(*)
FROM MICCAI.HUMANMASK
GROUP BY image;

-- Find the overlap of masks for each user for all images
INSERT INTO MICCAI.maskintersection(user, image, intersection)
SELECT b.user, a.image, count(*) AS INTERSECTION FROM
MICCAI.HUMANMASK a, MICCAI.USERMASK b
WHERE  
      a.image = b.image AND
      a.x = b.x AND
      a.y = b.y 
--    AND  b.user ='user100'
GROUP BY b.user, a.image
;
      
-- Find the union of masks for each user and image;
INSERT INTO MICCAI.maskunion(user, image, union)
SELECT b.user, a.image, count(*) AS UNION FROM
MICCAI.HUMANMASK a FULL JOIN MICCAI.USERMASK b ON   a.x = b.x AND a.y = b.y
WHERE  
      a.image = b.image 
GROUP BY b.user, a.image
;

:)


-- Computer Jarcardcofficient
SELECT a.intersection/b.union AS ratio
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image
ORDER BY ratio desc;
      

-- Find classification match
SELECT a.user, count(*) AS CORRECT_COUNT
FROM   MICCAI.classification a, MICCAI.classification b 
WHERE  a.image = b.image AND b.user = 'human' AND a.user <> 'human' AND
       UPPER(a.label) = UPPER(b.label)
GROUP BY a.user
ORDER BY CORRECT_COUNT DESC;
       
      









