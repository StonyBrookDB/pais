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
SELECT a.user, a.image, a.intersection/b.union AS ratio
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image
ORDER BY a.user desc;
      

-- Order by Jarcard cofficient for users based on total sum
SELECT a.user, SUM(a.intersection/b.union) AS TotalRatio
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image AND
      a.user <> 'human'
GROUP BY a.user 
ORDER BY TotalRatio desc;


-- Find classification label for a user
SELECT a.user, a.label, b.label,
CASE a.label
	WHEN  b.label THEN 'TRUE'
	ELSE 'FALSE'
END CORRECTNESS
FROM   MICCAI.classification a, MICCAI.classification b
WHERE  a.image = b.image AND b.user = 'human' AND a.user <> 'human' AND       
       a.user = '100';




-- Find classification match order by correctness
SELECT a.user, count(*) AS CORRECT_COUNT
FROM   MICCAI.classification a, MICCAI.classification b 
WHERE  a.image = b.image AND b.user = 'human' AND a.user <> 'human' AND
       UPPER(a.label) = UPPER(b.label)
GROUP BY a.user
ORDER BY CORRECT_COUNT DESC;
       
      









