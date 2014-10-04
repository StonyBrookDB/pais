-- Those are discarded due to no results returned for non-necrosis cases.

CASE a.label
	WHEN  b.label THEN 'TRUE'
	ELSE 'FALSE'
END CORRECTNESS

SELECT a.user, SUM(a.intersection/b.union) AS TotalRatio
	
	
SELECT a.user, a.intersection, b.union
CASE 

FROM MICCAI.maskintersection a LEFT OUTER JOIN MICCAI.maskunion b
     ON  a.image = b.image 
WHERE a.user = b.user AND
      a.user <> 'human'
GROUP BY a.user 
ORDER BY TotalRatio desc;


FROM
MICCAI.HUMANMASK a FULL JOIN MICCAI.USERMASK b ON   a.x = b.x AND a.y = b.y



122, 12, 68

--correct count

       
       
SELECT count(*) AS CORRECT_COUNT
FROM   MICCAI.classification b, MICCAI.classification a
WHERE  b.user = 'human' AND  a.user='100' AND
       a.image = b.image AND
       UPPER(a.label) = UPPER(b.label)       


-- incorrect count
SELECT count(*) AS CORRECT_COUNT
FROM   MICCAI.classification b LEFT OUTER JOIN MICCAI.classification a
       ON b.image = a.image 
WHERE  b.user = 'human' AND  a.user='100' AND
       UPPER(a.label) != UPPER(b.label)
       
-- Total count:   
SELECT count(*) AS TOTAL_COUNT
FROM MICCAI.classification b
WHERE b.user='human';



SELECT a.user, count(*) AS CORRECT_COUNT
FROM   MICCAI.classification b LEFT OUTER JOIN MICCAI.classification a
       ON b.image = a.image 
WHERE  b.user = 'human' AND a.user <> 'human' AND
       UPPER(a.label) = UPPER(b.label)
GROUP BY a.user
ORDER BY CORRECT_COUNT DESC;




--Total count
SELECT count(*) AS TOTAL_COUNT
FROM MICCAI.classification b
WHERE b.user='human';


-- Correct count
SELECT a.user, count(*)
AS CORRECT_COUNT_RATIO
FROM   MICCAI.classification b, MICCAI.classification a
WHERE  b.user = 'human' AND  a.user != 'human' AND
       a.image = b.image AND
       UPPER(a.label) = UPPER(b.label)  
GROUP BY a.user
ORDER BY CORRECT_COUNT_RATIO DESC;


-- Correct count ratio
SELECT a.user, count(*)*1.0/ 
(
SELECT count(*) AS TOTAL_COUNT
FROM MICCAI.classification b
WHERE b.user='human'
) 
AS CORRECT_COUNT_RATIO
FROM   MICCAI.classification b, MICCAI.classification a
WHERE  b.user = 'human' AND  a.user != 'human' AND
       a.image = b.image AND
       UPPER(a.label) = UPPER(b.label)  
GROUP BY a.user
ORDER BY CORRECT_COUNT_RATIO DESC;


 -- Correct count ratio formatted      
SELECT a.user, 
CAST(
count(*)*1.0/ 
(
SELECT count(*) AS TOTAL_COUNT
FROM MICCAI.classification b
WHERE b.user='human'
) 
AS DECIMAL(5, 3) )
AS CORRECT_COUNT_RATIO 
FROM   MICCAI.classification b, MICCAI.classification a
WHERE  b.user = 'human' AND  a.user != 'human' AND
       a.image = b.image AND
       UPPER(a.label) = UPPER(b.label)  
GROUP BY a.user
ORDER BY CORRECT_COUNT_RATIO DESC;


-- Necrosis evalation

SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE i.intersection
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE u.UNION
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 1 AND
   i.user = u.user 
--  AND i.user ='100'
ORDER BY i.user;	


-- Non-necrosis region
SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE d.x*d.y - u.union
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE d.x*d.y - i.intersection
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 0 AND
   i.user = u.user 
--  AND i.user ='100'
ORDER BY i.user;	



-- Get jarcard cofficient for all users and images
WITH NECROSIS AS(
SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE i.intersection
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE u.UNION
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 1 AND
   i.user = u.user 
),
NONNECROSIS AS(
SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE d.x*d.y - u.union
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE d.x*d.y - i.intersection
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 0 AND
   i.user = u.user 
)
SELECT y.user, y.image, y.intersection/union as jc FROM NECROSIS y
UNION
SELECT n.user, n.image, n.intersection/union as jc FROM NONNECROSIS n





-- sort total average

WITH NECROSIS AS(
SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE i.intersection
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE u.UNION
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 1 AND
   i.user = u.user 
),
NONNECROSIS AS(
SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE d.x*d.y - u.union
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE d.x*d.y - i.intersection
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 0 AND
   i.user = u.user 
),
J AS(
SELECT y.user, y.image, y.intersection/union as jc FROM NECROSIS y
UNION
SELECT n.user, n.image, n.intersection/union as jc FROM NONNECROSIS n
)
SELECT j.user, avg (j.jc) as avg_jarcard
FROM j
GROUP BY j.user
ORDER BY avg_jarcard DESC;



-- Get jarcard cofficient for one user, one image
WITH NECROSIS AS(
SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE i.intersection
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE u.UNION
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 1 AND
   i.user = u.user 
),
NONNECROSIS AS(
SELECT i.user, u.image, 
CASE i.image
	WHEN  NULL THEN 0
	ELSE d.x*d.y - u.union
END INTERSECTION,
CASE u.image
	WHEN  NULL THEN 100
	ELSE d.x*d.y - i.intersection
END UNION
FROM MICCAI.humanmaskdimension d LEFT OUTER JOIN MICCAI.maskintersection i ON d.image = i.image  LEFT OUTER JOIN MICCAI.maskunion u ON u.image = d.image
WHERE 
   d.necrosis = 0 AND
   i.user = u.user 
),
J AS(
SELECT y.user, y.image, y.intersection/union as jc FROM NECROSIS y
UNION
SELECT n.user, n.image, n.intersection/union as jc FROM NONNECROSIS n
)
SELECT j.user, j.jc as jarcard_cofficient
FROM j
WHERE j.user = '100' AND j.image ='path-image-125';
