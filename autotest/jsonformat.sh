#!/bin/bash

#file.json

#1.jpg
#2.jpg

#formatjson.json
# [
#   "1.jpg",
#   "2.jpg"
# ]

cat $1 | sed 's/$/&",/g'| sed 's/^/"&/g'| sed '1s/"/["/' | sed '$s/",/"]/' > formatjson.json
