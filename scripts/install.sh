echo 'Installing RunWeatherForecast'

rm -r /home/ec2-user/runForecast
mv /home/ec2-user/RunForecast  /home/ec2-user/runForecast

cd /home/ec2-user/runForecast/
cd RunForecast

rm -f /var/log/RunForecast.log
rm -f /var/log/forecastTriggerDocker.log
mvn -e clean install >> /var/log/RunForecast.log
#cp target/*.war /usr/share/tomcat7/webapps/ >> /var/log/tomcat.log
#cd  /usr/share/tomcat7/bin

docker build -t runweatherforecast .
docker rmi -f $(docker images -f "dangling=true" -q)
docker run -p 8081:8080 --name RunWeatherForecast runweatherforecast >> /var/log/runweatherforecast.log 2>&1 &
#sh ./start.sh >> /var/log/tomcat.log 2>&1 &
