echo 'Installing ForecastTrigger' 

rm -r /home/ec2-user/forecastTrigger
mv /home/ec2-user/ForecastTrigger  /home/ec2-user/forecastTrigger

sudo rm -r /var/log/forecastTriggerDocker.log
sudo rm -r /var/log/ForecastTrigger.log

cd /home/ec2-user/forecastTrigger/
chmod 777 forecasttrigger
cd StormCheck

mvn -e clean install -Dmaven.test.skip=true -DskipTests=true >> /var/log/ForecastTrigger.log
#cp target/*.war /usr/share/tomcat7/webapps/ >> /var/log/tomcat.log
#cd  /usr/share/tomcat7/bin
docker build -t forecasttrigger . >> /var/log/forecastTriggerDocker.log
docker rmi -f $(docker images -f "dangling=true" -q) >> /var/log/forecastTriggerDocker.log
docker run -p 8080:8080 --name StormChecklatest forecasttrigger >> /var/log/forecastTriggerDocker.log 2>&1 &

#sh ./start.sh >> /var/log/tomcat.log 2>&1 &

