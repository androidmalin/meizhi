#!/bin/bash

trap "record" 2

# capture screen of android device
androidScreenCapture() {
    curTime=`date +%Y-%m-%d-%H-%M-%S`
    tmpName="${curTime}.png"
    [[ -n $1 ]] && fileName=$1 || fileName=${tmpName}
    devicePath="/sdcard/${tmpName}"
    adb shell screencap -p ${devicePath}
    adb pull ${devicePath} ${fileName}
    adb shell rm ${devicePath}
}
curTime="";
export ADB_cleaSHELL_SCREEN_RECORD_ARGS='--verbose --bit-rate 2000000'
# record screen of android device
androidScreenRecord() {
    echo -e "\033[1m(press Ctrl-C to stop recording)\033[0m"
    curTime=`date +%Y-%m-%d-%H-%M-%S`
    tmpName="${curTime}.mp4"
    [[ -n $1 ]] && fileName=$1 || fileName=${tmpName}
    devicePath="/sdcard/${tmpName}"
    echo "24" ${devicePath}
    adb shell screenrecord ${ADB_SHELL_SCREEN_RECORD_ARGS} ${devicePath}
}

function record() {
    tmpName="${curTime}.mp4"
    devicePath="/sdcard/${tmpName}"
    echo "32" ${devicePath}
    echo "33" ${fileName}
    echo "34 wait for video encoding finish"
    sleep 1 # wait for video encoding finish
    adb pull ${devicePath} ${fileName}
    echo "36 pull video from sdcard"
    # Don't delete copy in device.
    # adb shell rm $devicePath
}


function asc() {
    if [[ -z $1 ]]; then
        echo "Please provide a filename."
        echo "Providing .png extension for capturing the device screen, and providing .mp4 for recording the device screen."
        return
    fi

    if [[ $1 == *.png ]]; then
        androidScreenCapture $1
    elif [[ $1 == *.mp4 ]]; then
        androidScreenRecord $1
    else
        echo "Filename with unKnow extension, only .png and .mp4 are supported"
    fi
}

asc $1
