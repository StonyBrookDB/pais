SELECT count(*) FROM
MICCAI.MASK a, MICCAI.MASK b
WHERE a.user ='human' AND b.user ='human' AND
      a.x = b.x AND
      a.y = b.y;
