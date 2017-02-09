#!/bin/bash
for (( i=0; i<=500; i++ ))
do
adb shell input swipe 100 1000 1400 1000
done
