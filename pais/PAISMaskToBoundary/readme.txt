README:
To convert a mask (in tiff or jpeg) into its mask representation, simply execute ./processMask imageName 1  (this will perform necessary boundary fixing)
or 
 ./processMask imageName (this will NOT perform boundary fixing after extracting mask)

The output should be polygon pairs of coordinates in a format that can be imported to DB2.

A sample of script to run images inside one directory is in: outputAll.sh
