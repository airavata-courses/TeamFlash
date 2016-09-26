echo 'starting installation process' >> /var/log/teamflash-stormdetection-install.log
cd '/home/ec2-user/StormDetection/StormDetection'

echo 'Activating virtualenv for StormDetection Microservice' >> /var/log/teamflash-stormdetection-install.log
pip install virtualenv >> /var/log/teamflash-stormdetection-install.log
virtualenv venv >> /var/log/teamflash-stormdetection-install.log
. venv/bin/activate >> /var/log/teamflash-stormdetection-install.log
pip install requests
pip install Flask >> /var/log/teamflash-stormdetection-install.log
export FLASK_APP=StormDetection.py
flask run --host=0.0.0.0 --port=9000 >> /var/log/teamflash-stormdetection-server.log 2>&1 &

