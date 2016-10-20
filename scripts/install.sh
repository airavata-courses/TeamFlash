echo 'Installing ForecastTrigger' 
cd '/home/ec2-user/ForecastTrigger/StormCheck'
mvn -e clean install >> /var/log/ForecastTrigger.log
#cp target/*.war /usr/share/tomcat7/webapps/ >> /var/log/tomcat.log
#cd  /usr/share/tomcat7/bin
docker build -t forecasttrigger .
docker rmi -f $(docker images -f "dangling=true" -q)
docker run -p 8080:8080 --name StormCheck forecasttrigger >> /var/log/forecastTriggerDocker.log 2>&1 &
#sh ./start.sh >> /var/log/tomcat.log 2>&1 &
