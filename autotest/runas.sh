adb shell
run-as meizhi.meizhi.malin
ls -l
cd shared_prefs
ls -l
cat umeng_general_config.xml
cat umeng_general_config.xml > /sdcard/umeng.xml
exit
exit
adb pull /sdcard/umeng.xml
cat umeng.xml
