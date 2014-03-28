#!/bin/sh    
 for dir in `ls -A $1`    
    do   
      for file in 'ls -A $1/$dir'
	   do
        tar -zcvf "$1/$dir/$file.tar.gz" "$1/$dir/$file"
	   done
	  
    done    
