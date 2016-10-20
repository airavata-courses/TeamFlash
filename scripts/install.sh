echo 'Installing RunWeatherForecast' 
cd '/home/ec2-user/RunForecast/RunForecast'
rm -f /var/log/RunForecast.log
rm -f /var/log/forecastTriggerDocker.log
mvn -e clean install >> /var/log/RunForecast.log
#cp target/*.war /usr/share/tomcat7/webapps/ >> /var/log/tomcat.log
#cd  /usr/share/tomcat7/bin

docker build -t forecasttrigger .
docker run -p 8081:8080 --name StormCheck forecasttrigger >> /var/log/forecastTriggerDocker.log 2>&1 &

#sh ./start.sh >> /var/log/tomcat.log 2>&1 &
