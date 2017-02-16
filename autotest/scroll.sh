#!/bin/bash
for (( i=0; i<=60; i++ ))
do
adb shell input swipe 300 2300 300 10
done
