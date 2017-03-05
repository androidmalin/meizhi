#!/bin/bash
#显示进程里的所有子线程
adb shell ps -t `adb shell ps | grep "meizhi.meizhi.malin" | awk '{print$2}'`
