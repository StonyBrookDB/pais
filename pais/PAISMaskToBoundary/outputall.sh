#! /bin/bash

# e.g. user001/algo1/mask1_0.ppm for normal region
#      user001/algo1/mask1_1.ppm for necrosis

outputprefix=output

mkdir -p outputAll
for file in data/*/algo*/*;
do
	echo "Processing "${file}
	image_name=`basename "$file" .tiff`
	directory_name=${file%/*}
	#echo $image_name
	#echo $directory_name
	echo $file | cut -d'/' -f2 > tmpFile
	read usrid < tmpFile
	rm tmpFile

	echo $file | cut -d'/' -f3 > tmpFile
	read algoname < tmpFile
	rm tmpFile
#	mkdir -p output/${directory_name}
	./processMask ${file} 2 | sed '/^$/d' > outputAll/${usrid}_${algoname}_${image_name}.txt

done
