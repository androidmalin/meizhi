#!/bin/bash
#将debugable=true的应用中/data/data/packagename/中的文件夹拉到电脑中
#./exec_out.sh -p com.example.myapplication -d databases
#./exec_out.sh -p tv.panda.live.broadcast -d files
#./exec_out.sh -p tv.panda.live.broadcast -d databases

#要拉单个文件的话可以使用
#adb exec-out run-as tv.panda.live.broadcast cat files/cover/cover.jpg > ./cover.jpg

P=
F=
D=

function usage()
{
	echo "$(basename $0) [-f file] [-d directory] -p package"
	exit 1
}

while getopts ":p:f:d:" opt
do
	case $opt in
		p)
			P=$OPTARG
			echo package is $OPTARG
			;;
		f)
			F=$OPTARG
			echo file is $OPTARG
			;;
		d)
			D=$OPTARG
			echo directory is $OPTARG
			;;
		\?)
			echo Unknown option -$OPTARG
			usage
			;;
		\:)
			echo Required argument not found -$OPTARG
			usage
			;;
	esac
done

[ x$P == x ] && {
    echo "package can not be empty"
	usage
	exit 1
}

[[ x$F == x  &&  x$D == x ]] && {
    echo "file or directory can not be empty"
	usage
	exit 1
}

function file_type()
{
	# use printf to avoid carriage return
	__t=$(adb shell run-as $P "sh -c \"[ -f $1 ] && printf f || printf d\"")
	echo $__t
}

function list_and_pull()
{
	t=$(file_type $1)
	if [ $t == d ]; then
		for f in $(adb shell run-as $P ls $1)
		do
			# the carriage return output from adb shell should
			# be removed
			mkdir -p $(echo -e $1 |sed $'s/\r//')
			list_and_pull $(echo -e $1/$f |sed $'s/\r//')
		done
	else
		echo pull file $1
		[ ! -e $(dirname $1) ] && mkdir -p $(dirname $1)
		$(adb exec-out run-as $P cat $1 > $1)
	fi
}

[ ! -z $D ] && list_and_pull $D
[ ! -z $F ] && list_and_pull $F
