#!/bin/bash
file=$1
if [ "${file:0:1}" != "/" ]; then 
	file=$(cd "$(dirname "$1")"; pwd)/$(basename "$1")
fi
echo "Opening $file in the ICE Plot Editor."

postStr="post={\"item_id\":\"-1\", \"posts\":[{\"type\":\"FILE_PLOTEDITOR\",\"message\":\"$file\"}]}"
echo $postStr | curl -u ice:veryice -d @- http://localhost:$2/ice/update
printf "\n"
