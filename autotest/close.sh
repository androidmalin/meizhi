#!/bin/bash
for (( i=0; i<=60; i++ ))
do
adb shell am start meizhi.meizhi.malin/.activity.MainActivity && sleep 1 && adb shell input keyevent 4
done
