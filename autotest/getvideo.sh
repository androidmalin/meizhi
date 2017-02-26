#!/bin/bash
export ADB_SHELL_SCREENRECORD_ARGS='--verbose --bit-rate 2000000'
echo -e "\033[1m(press Ctrl-C to stop recording)\033[0m"
curTime=`date +%Y-%m-%d-%H-%M-%S`
tmpeName="$curTime.mp4"
[[ -n $1 ]] && fileName=$1 || fileName=${tmpeName}
devicePath="/sdcard/$tmpeName"

echo -e "adb pull " ${devicePath}
adb shell screenrecord ${ADB_SHELL_SCREENRECORD_ARGS} $devicePath
#sleep 1 # wait for video encoding finish
adb pull ${devicePath}

# Don't delete copy in device.
# adb shell rm $devicePath
open ${fileName}
