echo 'killing existing flask process if any running' >> /var/log/flask-before-detection.log
#ps -ef | grep python | grep -v grep | awk '{print $2}' | xargs kill >> /var/log/flask-before.log
kill $(lsof -t -i:7000) >> /var/log/flask-before-detection.log
sleep 5
