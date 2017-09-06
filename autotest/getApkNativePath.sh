#!/bin/bash
#获取apk安装后so文件所在的路径
#./getpath.sh tv.panda.live.broadcast

packagename=$1

if [ ! -n "$packagename" ];then
	echo "$(basename $0) [ packagename ]"
	exit 1
fi

dname=""
dname=`adb shell pm path $packagename | cut -d ":" -f 2`
echo $dname
dname=`echo ${dname%/*}`"/lib/arm/"
echo $dname
