#!/bin/bash
function back(){
	java -jar abe.jar unpack meizhi.meizhi.malin.ab meizhi.meizhi.malin.tar $1;
	mkdir meizhibackup;
	mv meizhi.meizhi.malin.ab ./meizhibackup;
	mv meizhi.meizhi.malin.tar ./meizhibackup;
	cd meizhibackup;
	tar -xvf meizhi.meizhi.malin.tar;
}

if [ -z $1 ];then  
    echo "please input password"
else
    back $1
fi

#必须在调用函数地方之前，声明函数，shell脚本是逐行运行。不会像其它语言一样先预编译