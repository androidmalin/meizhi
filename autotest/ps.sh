#!/bin/bash
adb shell ps | grep "meizhi.meizhi.malin" | awk '{print$2}'
