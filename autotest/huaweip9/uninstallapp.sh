package=`aapt dump badging $* | grep package | awk '{print $2}' | sed s/name=//g | sed s/\'//g`
echo
echo package : $package
echo
echo Uninstall application .....
echo
adb uninstall $package
