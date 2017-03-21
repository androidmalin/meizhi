
#参数可以是apk,jar,dex
dexdump -f $1 | grep "method_ids_size"

#http://stackoverflow.com/questions/16056442/cant-build-multiple-android-dex-files-with-ant-from-external-jars/16766238#16766238
#或者如下的命令
#dx --dex --output=temp.dex library.jar
#cat temp.dex | head -c 92 | tail -c 4 | hexdump -e '1/4 "%d\n"'
