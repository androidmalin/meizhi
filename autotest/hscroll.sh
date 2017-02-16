#!/bin/bash
for (( i=0; i<=500; i++ ))
do
adb shell input swipe 10 1000 1430 1000
done
