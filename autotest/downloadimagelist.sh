#!/bin/bash
cat image2.json | jq . | sed 's/\"//g'| sed -e 's/[ ][ ]*//g' | sed 's/,//g' | sed 's/]//g' | xargs wget -c -P ./image2
