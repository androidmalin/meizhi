#!/bin/bash
adb shell am force-stop meizhi.meizhi.malin && \
gradle installBaiduDebug -x lint --build-cache --daemon --parallel --offline --continue && \
adb shell am start meizhi.meizhi.malin/meizhi.meizhi.malin.activity.MainActivity
