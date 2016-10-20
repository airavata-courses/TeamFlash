echo 'killing existing flask process if any running' >> /var/log/flask-before-clustering.log
docker ps -a | grep 'stormclustering' | awk '{print $1}' | xargs --no-run-if-empty docker stop >> /var/log/flask-before-clustering.log
docker ps -a | grep 'stormclustering' | awk '{print $1}' | xargs --no-run-if-empty docker rm >> /var/log/flask-before-clustering.log
#ps -ef | grep python | grep -v grep | awk '{print $2}' | xargs kill >> /var/log/flask-before.log
#kill $(lsof -t -i:8000) >> /var/log/flask-before-clustering.log
#sleep 5
