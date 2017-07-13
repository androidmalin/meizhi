#!/bin/bash
name=$1
cat $name | jq .
#python -m json.tool aaa.json
#cat aaa.json | jq .
#https://jqplay.org
