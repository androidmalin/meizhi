#!/bin/bash
adb shell cmd package dump-profiles meizhi.meizhi.malin && \
sleep 1 && \
adb pull /data/misc/profman/meizhi.meizhi.malin.txt
