echo 'starting installation process' >> /var/log/teamflash-stormclustering-install.log
cd '/home/ec2-user/PythonServicesDeploy/StormClustering'
echo 'Activating virtualenv for StormClustering Microservice' >> /var/log/teamflash-stormclustering-install.log
pip install virtualenv >> /var/log/teamflash-stormclustering-install.log
#virtualenv venv >> /var/log/teamflash-stormclustering-install.log
virtualenv --python=/usr/bin/python3 venv >> /var/log/teamflash-stormclustering-install.log
. venv/bin/activate >> /var/log/teamflash-stormclustering-install.log
pip install requests >> /var/log/teamflash-stormclustering-install.log
pip install request >> /var/log/teamflash-stormclustering-install.log
pip install Flask >> /var/log/teamflash-stormclustering-install.log
export FLASK_APP=StormClustering.py
flask run --host=0.0.0.0 --port=8000 >> /var/log/teamflash-stormclustering-server.log 2>&1 &

echo 'starting installation process' >> /var/log/teamflash-stormdetection-install.log
cd '/home/ec2-user/PythonServicesDeploy/StormDetection'
echo 'Activating virtualenv for StormDetection Microservice' >> /var/log/teamflash-stormdetection-install.log
pip install virtualenv >> /var/log/teamflash-stormdetection-install.log
#virtualenv venv >> /var/log/teamflash-stormdetection-install.log
virtualenv --python=/usr/bin/python3 venv >> /var/log/teamflash-stormdetection-install.log
. venv/bin/activate >> /var/log/teamflash-stormdetection-install.log
pip install requests >> /var/log/teamflash-stormdetection-install.log
pip install request >> /var/log/teamflash-stormdetection-install.log
pip install Flask >> /var/log/teamflash-stormdetection-install.log
export FLASK_APP=StormDetection.py
flask run --host=0.0.0.0 --port=7000 >> /var/log/teamflash-stormdetection-server.log 2>&1 &
