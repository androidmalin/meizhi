#!/bin/bash
name=$1
aapt dump badging $name | grep launchable-activity: | awk '{print $2}' | sed s/name=//g | sed s/\'//g
