
#!/bin/bash
ip=`adb shell ip a s | grep -w inet | awk '{ print $2}' | awk 'NR==2{print}' | cut -d '/' -f 1`
adb disconnect $ip
