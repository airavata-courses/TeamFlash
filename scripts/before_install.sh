echo 'killing existing node process if any running' >> /var/log/node-before.log
ps -ef | grep node >> /var/log/node-before.log
pkill -f node >> /var/log/node-before.log
sleep 5
