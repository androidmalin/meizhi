#!/bin/bash
#./getnativesofile.sh com.panda.videoliveplatform nativeSoList.txt
#包名
#so文件列表 
#so文件列条如下

#libBugly.so
#libcocklogic.so
#libhpplaysmdns.so

#可以配合getApkNativePath.sh使用
#adb shell cd $path  cd /data/app/com.panda.videoliveplatform-1/lib/arm/  ls
packagename=$1
filename=$2
if [ ! -n "$packagename" ];then
	echo "$(basename $0) [ packagename ] [ nativelistfile]"
	exit 1
fi

if [ ! -n "$filename" ];then
	echo "$(basename $0) [ packagename ] [ nativelistfile]"
	exit 1
fi

dname=""
dname=`adb shell pm path $packagename | cut -d ":" -f 2`
echo $dname
dname=`echo ${dname%/*}`
echo "dname:"$dname

path=$dname"/lib/arm/"

echo "path="$path
filename=$filename

echo "filename="$filename
name=""
rm -rf $packagename
mkdir $packagename
i=0
if [ -f ${filename} ];then
   while read line
   do
     arr=($line)
     name=$path${arr[0]}
     echo $name
     adb pull $name ./$packagename
     i=$((i+1))
   done < ${filename}
fi
