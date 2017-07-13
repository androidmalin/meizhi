#!/bin/bash
#adb shell ifconfig | grep "inet addr:10" | cut -d ":" -f 2 | cut -d " " -f 1
adb shell ifconfig | grep "inet addr" | grep "Bcast" | cut -d ":" -f 2 | cut -d " " -f 1
