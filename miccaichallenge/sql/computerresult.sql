
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


INSERT INTO MICCAI.maskunion(user, image, union)
SELECT u.user, u.image, u.count + h.count - i.intersection
FROM miccai.usermaskcount u,  miccai.humanmaskcount h,
   miccai.maskintersection i
WHERE u.image = h.image and i.image = u.image and i.user=u.user;



(:      
-- Find the union of masks for each user and image;
INSERT INTO MICCAI.maskunion(user, image, union)
SELECT b.user, a.image, count(*) AS UNION FROM
MICCAI.HUMANMASK a FULL JOIN MICCAI.USERMASK b ON   a.x = b.x AND a.y = b.y
WHERE  
      a.image = b.image 
--    AND  b.user ='user100'
GROUP BY b.user, a.image
;
:)