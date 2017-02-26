#!/bin/bash
curTime=`date +%Y-%m-%d-%H-%M-%S`
tmpName="$curTime.png"
[[ -n $1 ]] && fileName=$1 || fileName=${tmpName}
devicePath="/sdcard/$tmpName"
adb shell screencap -p ${devicePath}
adb pull ${devicePath} ${fileName}
adb shell rm ${devicePath}

#截取Android设备当前屏幕并保存到执行指令的电脑目录上：
#http://blog.iderzheng.com/android-screen-capture-from-terminal/