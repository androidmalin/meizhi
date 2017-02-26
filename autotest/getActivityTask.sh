#!/bin/bash
adb shell dumpsys activity activities | sed -En -e '/Running activities/,/Run #0/p'
