#!/bin/bash
tshark -s 512 -i enp3s0 -n -f 'ip src 10.31.4.116 && ip dst 10.31.146.12' -Y 'http.request.uri' -T fields -e http.request.uri -l | tr -d '\t'
