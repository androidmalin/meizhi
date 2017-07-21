#!/bin/bash
hexdump -C -n 512 pm.img

#hexdump -C -n 512 cover.jpg | awk '{for(i=NF; i>17; --i) print $i}'

##1.od -x pm.img # -x：用十六进制显示
##2.hexdump -C -n 512 pm.img # -n 512 ；只显示前512个字节的数据 -C：左边显示16进制，右边显示字符
##3.xxd -u -a -g 1 -c 16 -s +0x2600 -l 512 x.img #显示从0x2600 开头的512字节 ，每排显示16个字节
