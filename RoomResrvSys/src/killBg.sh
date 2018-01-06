kill $(ps aux | grep -e $* | awk '{print $2}')
