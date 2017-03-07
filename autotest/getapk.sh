#!/bin/bash
name=`adb shell pm path meizhi.meizhi.malin | cut -d ':' -f 2` \
&& sleep 0.1 &&
adb pull $name