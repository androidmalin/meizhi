#!/bin/bash
tshark -s 512 -i enp3s0 -f 'ip src 10.31.146.12 && ip dst 10.31.4.116' -Y 'tcp.port' -T fields -e tcp.port -l | tr -d '\t'



#tshark -i enp3s0 -f 'tcp port 80 and (((ip[2:2] - ((ip[0]&0xf)<<2)) - ((tcp[12]&0xf0)>>2)) != 0)' -Y 'http.request.method == "GET" || http.request.method == "HEAD"' -l | tr -d '\t'

tshark -s 512 -i enp3s0 -n -f 'tcp dst port 3306' -Y 'mysql.query' -T fields -e mysql.query


tshark -i enp3s0 -n -t a -Y http.request -T fields -e "frame.time" -e "frame.time" -e "ip.src" -e "http.host" -e "http.request.method" -e "http.request.uri"
