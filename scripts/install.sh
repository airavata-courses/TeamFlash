echo 'starting installation process' >> /var/log/teamflash-stormclustering-install.log
cd '/home/ec2-user/StormClustering/'
docker build --no-cache=true -t stormclustering . >> /var/log/teamflash-stormclustering-docker-install.log
docker run --name stormclustering -p 8000:8000 stormclustering >> /var/log/teamflash-stormclustering-docker-server.log


#echo 'Activating virtualenv for StormClustering Microservice' >> /var/log/teamflash-stormclustering-install.log
#pip install virtualenv >> /var/log/teamflash-stormclustering-install.log
#virtualenv venv >> /var/log/teamflash-stormclustering-install.log
#. venv/bin/activate >> /var/log/teamflash-stormclustering-install.log
#pip install requests >> /var/log/teamflash-stormclustering-install.log
#pip install request >> /var/log/teamflash-stormclustering-install.log
#pip install markupsafe >> /var/log/teamflash-stormclustering-install.log
#pip install Flask >> /var/log/teamflash-stormclustering-install.log
#export FLASK_APP=StormClustering.py
#flask run --host=0.0.0.0 --port=8000 >> /var/log/teamflash-stormclustering-server.log 2>&1 &

