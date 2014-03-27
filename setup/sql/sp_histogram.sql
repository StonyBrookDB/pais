DROP FUNCTION PAIS.plgn2str@

DROP PROCEDURE pais.histogram_area@
DROP PROCEDURE pais.histogram_circularity@
DROP PROCEDURE pais.histogram_eccentricity@
DROP PROCEDURE pais.histogram_energy@
DROP PROCEDURE pais.histogram_entropy@
DROP PROCEDURE pais.histogram_max_intensity@
DROP PROCEDURE pais.histogram_mean_intensity@
DROP PROCEDURE pais.histogram_min_intensity@
DROP PROCEDURE pais.histogram_perimeter@
DROP PROCEDURE pais.histogram_std_intensity@

DROP PROCEDURE pais.gen_all_histogram_area @
DROP PROCEDURE pais.gen_all_histogram_circularity @
DROP PROCEDURE pais.gen_all_histogram_eccentricity @
DROP PROCEDURE pais.gen_all_histogram_energy @
DROP PROCEDURE pais.gen_all_histogram_entropy @
DROP PROCEDURE pais.gen_all_histogram_max_intensity @
DROP PROCEDURE pais.gen_all_histogram_mean_intensity @
DROP PROCEDURE pais.gen_all_histogram_min_intensity @
DROP PROCEDURE pais.gen_all_histogram_perimeter @
DROP PROCEDURE pais.gen_all_histogram_std_intensity @

CREATE FUNCTION PAIS.plgn2str(polygon DB2GSE.ST_POLYGON)
RETURNS VARCHAR(30000)
LANGUAGE SQL
DETERMINISTIC
NO EXTERNAL ACTION
BEGIN ATOMIC
DECLARE del       VARCHAR(30000) default ')';
DECLARE plgnstr   VARCHAR(30000);
DECLARE length    INT;
DECLARE position  INT default 0;

SET plgnstr = CAST (polygon..ST_AsText AS  varchar(30000) );
SET length = length(plgnstr);
RETURN substr (plgnstr, 11, length - 12);

END@



CREATE PROCEDURE pais.histogram_area(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start INTEGER, IN 
end INTEGER, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'area', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT area,INTEGER((area-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  area>=start AND area<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_circularity(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start DOUBLE, IN 
end DOUBLE, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'circularity', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT circularity,INTEGER((circularity-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  circularity>=start AND circularity<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@

CREATE PROCEDURE pais.histogram_eccentricity(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start DOUBLE, IN 
end DOUBLE, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'eccentricity', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start + binnum*(end-start)/number as binstart, 
      start + (binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT ECCENTRICITY,INTEGER((ECCENTRICITY-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  ECCENTRICITY>=start AND ECCENTRICITY<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_energy(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start INTEGER, IN 
end INTEGER, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'energy', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT energy,INTEGER((energy-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  energy>=start AND energy<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_entropy(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start INTEGER, IN 
end INTEGER, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'entropy', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT entropy,INTEGER((entropy-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  entropy>=start AND entropy<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_max_intensity(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start INTEGER, IN 
end INTEGER, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'max_intensity', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT max_intensity,INTEGER((max_intensity-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  max_intensity>=start AND max_intensity<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_mean_intensity(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start INTEGER, IN 
end INTEGER, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'mean_intensity', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT mean_intensity,INTEGER((mean_intensity-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  mean_intensity>=start AND mean_intensity<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_min_intensity(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start DOUBLE, IN 
end DOUBLE, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'min_intensity', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT min_intensity,INTEGER((min_intensity-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  min_intensity>=start AND min_intensity<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_perimeter(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start INTEGER, IN 
end INTEGER, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'perimeter', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT perimeter,INTEGER((perimeter-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  perimeter>=start AND perimeter<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.histogram_std_intensity(IN i_pais_uid VARCHAR(64), IN set_id INTEGER, IN start DOUBLE, IN 
end DOUBLE, IN number INTEGER)
LANGUAGE SQL
RESULT SETS 1
BEGIN
  --DECLARE C1 CURSOR WITH RETURN FOR
    insert into pais.feature_histogram
    SELECT i_pais_uid, 'std_intensity', set_id, (binnum+1) AS binnum,COUNT(*) AS frequency, 
      start+binnum*(end-start)/number as binstart, 
      start+(binnum+1)*(end-start)/number as binend 
    FROM 
      (SELECT std_intensity,INTEGER((std_intensity-start)*number/(end-start)) as binnum FROM PAIS.CALCULATION_FLAT 
       WHERE  std_intensity>=start AND std_intensity<end AND pais_uid = i_pais_uid) t 
    GROUP BY binnum;
  --OPEN C1;
END@


CREATE PROCEDURE pais.gen_all_histogram_area()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_area(i_uid, 1, 0, 400, 40);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @


CREATE PROCEDURE pais.gen_all_histogram_circularity()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_circularity(i_uid, 1, 0.0, 1.50, 30);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @

CREATE PROCEDURE pais.gen_all_histogram_eccentricity()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_eccentricity(i_uid, 1, 0.0, 1.0, 40);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @

CREATE PROCEDURE pais.gen_all_histogram_energy()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_energy(i_uid, 1, 0.0, 0.125, 30);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @

CREATE PROCEDURE pais.gen_all_histogram_entropy()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_entropy(i_uid, 1, 0.3, 1.30, 30);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @

CREATE PROCEDURE pais.gen_all_histogram_max_intensity()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_max_intensity(i_uid, 1, 0.178, 0.35, 30);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @

CREATE PROCEDURE pais.gen_all_histogram_mean_intensity()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_mean_intensity(i_uid, 1, 0.131, 0.268, 30);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @

CREATE PROCEDURE pais.gen_all_histogram_min_intensity()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_min_intensity(i_uid, 1, 0.0, 0.262, 30);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @

CREATE PROCEDURE pais.gen_all_histogram_perimeter()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_perimeter(i_uid, 1, 0.0, 200.0, 40);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @
CREATE PROCEDURE pais.gen_all_histogram_std_intensity()	
LANGUAGE SQL
BEGIN
  DECLARE i_uid VARCHAR(64);
  DECLARE SQLSTATE CHAR(5) DEFAULT '00000';
  DECLARE at_end 	INTEGER DEFAULT 0;
  DECLARE not_found 	CONDITION for SQLSTATE '02000';

  DECLARE c1 CURSOR WITH HOLD FOR
     SELECT pais_uid FROM PAIS.COLLECTION  WHERE methodname = 'NS-MORPH' and sequencenumber ='1';
  DECLARE CONTINUE HANDLER for not_found 
    SET at_end = 1;
  OPEN c1;  
  fetch_loop:
   LOOP
    FETCH c1 INTO  i_uid;
    IF at_end <> 0 THEN LEAVE fetch_loop; END IF;
    CALL pais.histogram_std_intensity(i_uid, 1, 0.0, 0.064, 30);
  END LOOP fetch_loop;
  CLOSE c1 WITH RELEASE;
END @
