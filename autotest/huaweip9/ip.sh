ip a s | grep -w inet | awk '{ print $2}' | awk 'NR==2{print}' | cut -d '/' -f 1
#ifconfig | grep "inet 地址:10" | awk '{print $2}' | cut -d ":" -f 2
