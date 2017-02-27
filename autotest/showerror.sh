#!/bin/bash
adb logcat -c && pidcat.py meizhi.meizhi.malin -t System.err -t RuntimeException
