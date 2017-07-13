name=`locate $1 | awk 'NR==2{print}'`
display -resize 500x500 $name
