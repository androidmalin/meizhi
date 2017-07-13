#获取Android 手机屏幕上显示的应用 包名
adb shell dumpsys input | grep FocusedApplication
#返回结果空就试试
#adb shell dumpsys window w | grep \\/ | grep name=
