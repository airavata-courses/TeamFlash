echo 'Installing ForecastTrigger' 
cd '/home/ec2-user/ForecastTrigger'
mvn -e clean install >> /var/log/ForecastTrigger.log
cp target/*.war /usr/share/tomcat7/webapps/ >> /var/log/tomcat.log
cd  /usr/share/tomcat7

sudo service tomcat7 start >> /var/log/tomcat.log 2>&1 &
