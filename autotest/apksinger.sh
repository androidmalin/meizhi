#查看apk文件签名
#参数为apk文件的地址
jarsigner -verify -verbose -certs $1
