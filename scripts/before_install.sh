echo 'killing existing flask process if any running' >> /var/log/flask-before-clustering.log
if [[ "$(docker images -q myimage:mytag 2> /dev/null)" == "" ]]; then
docker stop stormclustering
docker rm -f stormclustering
else
echo "No container present"
fi
#ps -ef | grep python | grep -v grep | awk '{print $2}' | xargs kill >> /var/log/flask-before.log
#kill $(lsof -t -i:8000) >> /var/log/flask-before-clustering.log
#sleep 5
