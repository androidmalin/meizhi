package=`aapt dump badging $* | grep package | awk '{print $2}' | sed s/name=//g | sed s/\'//g`
activity=`aapt dump badging $* | grep activity | awk '{print $2}' | sed s/name=//g | sed s/\'//g`
echo
echo package : $package
echo activity: $activity
echo
echo Launching application on device....
echo
adb shell am start -n $package/$activity
