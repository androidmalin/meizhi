#!/bin/bash
cd .. && sudo chown -R malin:malin . && adb uninstall meizhi.meizhi.malin && gradle installXiaomiRelease -x lint --configure-on-demand --daemon --parallel --offline && adb shell am start meizhi.meizhi.malin/.activity.MainActivity && \
 adb logcat -c && pidcat.py meizhi.meizhi.malin
