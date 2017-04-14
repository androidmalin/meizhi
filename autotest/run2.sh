#!/bin/bash
cd .. && sudo chown -R malin:malin . && adb uninstall meizhi.meizhi.malin && gradle installXiaomiDebug -x lint  --build-cache --configure-on-demand --daemon --parallel --offline && adb shell am start meizhi.meizhi.malin/.activity.TActivity && \
 adb logcat -c && pidcat.py meizhi.meizhi.malin
