#!/bin/bash
cd .. && gradle installXiaomiDebug -x lint  --build-cache --configure-on-demand --daemon --parallel --offline && adb shell am start meizhi.meizhi.malin/.activity.FrescoActivity && \
 adb logcat -c && pidcat.py meizhi.meizhi.malin
