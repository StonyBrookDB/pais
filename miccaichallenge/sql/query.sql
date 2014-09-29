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


-- Computer Jaccardcofficient
SELECT a.user, a.image, a.intersection/b.union AS ratio
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image
ORDER BY a.user desc;
      

-- Order by Jaccard cofficient for users based on total sum
SELECT a.user, SUM(a.intersection/b.union) AS TotalRatio
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image AND
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
       
   
--Rank by image and JC:
SELECT a.image, a.user, CAST( 1.0*a.intersection/b.union AS DECIMAL(5,3)  ) AS 
Jaccard_Cofficient
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image 
ORDER BY a.image, Jaccard_Cofficient desc;

--Rank ALL by JC:
SELECT CAST( 1.0*a.intersection/b.union AS DECIMAL(5,3)  ) AS 
Jaccard_Cofficient, a.image, a.user
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image 
ORDER BY Jaccard_Cofficient desc;

--Jaccard rank final:
SELECT a.user, CAST( AVG(1.0*a.intersection/b.union) AS DECIMAL(5,3) ) AS 
AVG_JC
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image 
GROUP BY a.user 
ORDER BY AVG_JC desc;



--Return jaccard cofficient for one user and one image:
SELECT CAST( 1.0*a.intersection/b.union AS DECIMAL(5,3)  ) AS 
Jaccard_Cofficient
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image AND
      a.image = 'path-image-213' AND
      a.user = 'dataset1';


-- Rank of images sorted by user:
SELECT a.user, CAST( SUM(1.0*a.intersection/b.union) AS DECIMAL(5,3) ) AS 
TotalRatio
FROM MICCAI.maskintersection a, MICCAI.maskunion b
WHERE a.user = b.user AND
      a.image = b.image 
GROUP BY a.user 
ORDER BY TotalRatio desc;
