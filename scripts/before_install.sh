echo 'killing existing flask process if any running' >> /var/log/flask-before-clustering.log
docker stop stormclustering
docker rm -f stormclustering
#ps -ef | grep python | grep -v grep | awk '{print $2}' | xargs kill >> /var/log/flask-before.log
#kill $(lsof -t -i:8000) >> /var/log/flask-before-clustering.log
#sleep 5
