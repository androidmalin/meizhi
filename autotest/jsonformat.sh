#!/bin/bash
cat $1 | sed 's/$/&",/g'| sed 's/^/"&/g'| sed '1s/"/["/' | sed '$s/",/"]/' > hhhh.json
