#!/bin/sh    
 for file in `ls -A $1`    
    do    
	  mkdir "$2/$file"
      ./tilegen "$1/$file" "$2/$file" 2 0
    done    
