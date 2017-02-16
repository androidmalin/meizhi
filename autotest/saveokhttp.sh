#!/bin/bash
rm http.log && adb logcat -c && pidcat.py meizhi.meizhi.malin -t OkHttp >> ./http.log
