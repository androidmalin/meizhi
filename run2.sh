#!/bin/bash
sudo chown -R malin:malin . && adb uninstall meizhi.meizhi.malin && gradle installRelease -x lint --daemon --parallel --offline && adb shell am start meizhi.meizhi.malin/.activity.MainActivity && adb logcat -c && pidcat.py meizhi.meizhi.malin
