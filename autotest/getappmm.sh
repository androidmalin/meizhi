#!/bin/bash
name=`adb shell getprop ro.build.version.sdk`
echo "version.sdk:"$name

name=`adb shell getprop ro.build.version.release`
echo "version.release:"$name

name=`adb shell getprop ro.product.brand`
echo "brand:"$name

name=`adb shell getprop ro.product.model`
echo "model:"$name

name=`adb shell getprop ro.product.manufacturer`
echo "manufacturer:"$name

name=`adb shell getprop ro.product.locale`
echo "locale:"$name

name=`adb shell getprop dalvik.vm.heapstartsize`
echo "heapstartsize:"$name

name=`adb shell getprop dalvik.vm.heapgrowthlimit`
echo "heapgrowthlimit:"$name

name=`adb shell getprop dalvik.vm.heapsize`
echo "heapsize:"$name


density=`adb shell getprop ro.sf.lcd_density`
echo "density:"$density

name=`adb shell wm size`
echo $name

name=`adb shell dumpsys window displays | grep "init" | awk '{print$4}' | cut -d '=' -f 2`
echo "app-content: "$name


high=`adb shell wm size | cut -d 'x' -f 2`
high2=`adb shell dumpsys window displays | grep "init" | awk '{print$4}' | cut -d '=' -f 2 | cut -d 'x' -f 2`
high3=$(( high - high2 ))

if [ $high3 -ne 0 ];then
    echo "Navigation-high:"$high3
fi

densitydd=`echo "scale=2;$density/160"|bc`

echo "densitydd:"$densitydd

navigationdp=`echo "scale=2;$high3/$densitydd"|bc`

echo "Navigation-high:"$navigationdp"dp"

ip=`adb shell ifconfig | grep Mask | awk 'NR==1{print}' | awk '{print $2}' | cut -d ':' -f 2`
echo "phone ip:"$ip

pc=`ip a s | grep -w inet | awk '{ print $2}' | awk 'NR==2{print}' | cut -d '/' -f 1`
echo "pc ip:"$pc