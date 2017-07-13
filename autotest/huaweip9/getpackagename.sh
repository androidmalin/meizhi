#!/bin/bash
#name=$1
#aapt dump xmltree $name AndroidManifest.xml | grep package | cut -d " " -f 6 | sed 's/\"//g' | cut -d "=" -f 2
name=$1
aapt dump badging $name | grep package | awk '{print $2}' | sed s/name=//g | sed s/\'//g
