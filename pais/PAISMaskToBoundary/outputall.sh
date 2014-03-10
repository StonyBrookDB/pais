#! /bin/bash

for file in data/*.ppm;
do
	echo "Processing "${file}
	./processMask ${file} 2 | sed '/^$/d' >> ${file}.boundary.txt

done
