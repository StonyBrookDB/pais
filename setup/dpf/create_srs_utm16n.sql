-- db2 -tf create_srs_utm16n.sql 
connect to testp1;
!db2se drop_srs testp1  -srsName UTM16N;

!db2se create_srs  testp1
  -srsId   100
  -srsName UTM16N
  -xOffset       0
  -yOffset       0
  -xScale      1
  -coordsysName WGS_1984_UTM_ZONE_16N
 ;
