#!/bin/bash
tshark -s 512 -i enp3s0 -n -f 'tcp dst port 80' -Y 'http.request.uri' -T fields -e http.host -e http.request.uri -l | tr -d '\t'

#-s 512 :只抓取前512个字节数据
#-i enp3s0 :捕获enp3s0网卡(可以通过ifconfig获取，或者 sudo lshw -class network | grep "logical name"）
#-n :禁止网络对象名称解析
#-f ‘tcp dst port 80’ :只捕捉协议为tcp,目的端口为80的数据包 过滤
#-Y ‘http.host and http.request.uri’ :过滤出http.host和http.request.uri  //display filter
#-T fields -e http.host -e http.request.uri :打印http.host和http.request.uri
#-l ：输出到标准输出
#-n 禁止所有地址名字解析（默认为允许所有）。


#详细查看用户对我们的主机进行那些请求方法
#tshark -i enp3s0 -n -t a -Y http.request -T fields -e "frame.time" -e "frame.time" -e "ip.src" -e "http.host" -e "http.request.method" -e "http.request.uri"


#Dec 15, 2016 18:09:58.369728131 CST	10.31.146.12	10.110.18.48:1084	POST	/send
#Dec 15, 2016 18:12:29.747772010 CST	10.31.146.12	10.110.18.48:1084	POST	/send
