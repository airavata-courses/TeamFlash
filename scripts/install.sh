echo 'Installing RunWeatherForecast' 
cd '/home/ec2-user/RunForecast/RunForecast'
mvn -e clean install >> /var/log/RunForecast.log
cp target/*.war /usr/share/tomcat7/webapps/ >> /var/log/tomcat.log
cd  /usr/share/tomcat7/bin

sh ./start.sh >> /var/log/tomcat.log 2>&1 &
