echo 'Installing ForecastTrigger' 
cd '/home/ec2-user/ForecastTrigger'
mvn -e clean install >> /var/log/ForecastTrigger.log
cp target/*.war /usr/local/tomcat7/apache-tomcat-7.0.72/webapps/ >> /var/log/tomcat.log
cd  /usr/local/tomcat7/apache-tomcat-7.0.72

sudo sh ./bin/startup.sh >> /var/log/tomcat.log 2>&1 &