INSERT INTO MICCAI.maskunion(user, image, union)
SELECT u.user, u.image, u.count + h.count - i.intersection
FROM miccai.usermaskcount u,  miccai.humanmaskcount h,
   miccai.maskintersection i
WHERE u.image = h.image and i.image = u.image and i.user=u.user;

