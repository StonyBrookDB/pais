#! /bin/bash

# e.g. user001/algo1/mask1_0.ppm for normal region
#      user001/algo1/mask1_1.ppm for necrosis

outputprefix=output

mkdir -p outputAll
for file in data/*/algo*/*;
do
	echo "Processing "${file}
	file_name=`basename "$file" .tiff`
	directory_name=${file%/*}
	#echo $image_name
	#echo $directory_name
	read usrid < <(echo $file | cut -d'/' -f2)

	read algoname < <(echo $file | cut -d'/' -f3 )
	
	read image_name < <( echo ${file_name} | cut -d'_' -f1 )
	read sequence < <( echo ${file_name} | tr '.' '_' | cut -d'_' -f2 )
	mkdir -p outputAll/${usrid}/${algoname}/${sequence}/
	
	./processMask ${file} 3 | sed '/^$/d' > outputAll/${usrid}/${algoname}/${sequence}/${image_name}.txt

done
