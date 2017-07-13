#!/bin/sh  
name=$1
for files in $(ls *.png)  
    do mv $files "$name"$files  
done 
