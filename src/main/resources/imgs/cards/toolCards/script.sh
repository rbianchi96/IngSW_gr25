a=0
for i in *.jpg; do
  new=$(printf "toolCard%d.png" "$a") #04 pad to length of 4
  mv -- "$i" "$new"
  let a=a+1
done
