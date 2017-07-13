
#!/bin/bash
 for (( i=8000; i<=9000; i++ ))
do
  http http://back.xiutuzz.com/album/browse13?albumID=$i | grep imageList | cut -d " " -f 4 | cut -d "=" -f 2 | jq . | sed 's/\"//g' | sed 's/\[//g' | sed 's/\]//g' | sed 's/\,//g' | sed '/^ *$/d' | sed 's/^[ \t]*//g' | xargs wget
done
