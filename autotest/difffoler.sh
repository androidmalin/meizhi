#!/usr/bin/env bash
# 比较两个文件夹是否一致
# 计算每个文件文件的md5值，将所有的md5值写入一个文件中，最后再用md5比较这个文件是否一样
# mac系统下计算md5的值命令是md5，如果是linux，请换成md5sum
# 用法：sh is_same.sh dir1 dir2

if [[ $# != 2 ]]; then
    echo "usage: sh is_same.sh <dir1> <dir2>"
    exit 1
fi

# find查找文件，md5计算md5值，awk取出md5值，sort保证次序是一致的，将结果输出到文件中
find $1 -type f -print | grep -v dir*.md5 | xargs md5sum | awk '{print $1}' | sort > temp.dir1.md5
find $2 -type f -print | grep -v dir*.md5 | xargs md5sum | awk '{print $1}' | sort > temp.dir2.md5

# 比较两个文件是否相同，如果相同说明两个目录是一致的
md5sum1=$(md5sum temp.dir1.md5 | awk '{print $1}')
md5sum2=$(md5sum temp.dir2.md5 | awk '{print $1}')

if [[ $md5sum1 != $md5sum2 ]]; then
    echo "$1 and $2 is different"
else
    echo "$1 and $2 is same"
fi

# 删除临时文件
rm temp.dir1.md5
rm temp.dir2.md5
