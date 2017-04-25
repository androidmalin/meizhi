# coding:utf-8
import re
import requests
import os
# 获取网页内容
r = requests.get('https://dl.bintray.com/android/android-tools/com/android/tools/annotations/25.3.1/')
data = r.text

# 利用正则查找所有连接
link_list =re.findall(r"(?<=href=\").+?(?=\")|(?<=href=\').+?(?=\')" ,data)
for url in link_list:
    print url
    commondStr = "wget -c"+" https://dl.bintray.com/android/android-tools/com/android/tools/annotations/25.3.1/"+url+" -O "+url
    os.system(commondStr)
