#!/bin/sh   
for file in `ls -A $1/*`    
    do    
      tar -zxvf "$1/$file.tar.gz" "$1/$file"
    done  

