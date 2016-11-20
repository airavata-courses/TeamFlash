echo 'Installing LoadBalancer' 

rm -r /home/ec2-user/loadBalancer
mv /home/ec2-user/LoadBalancer /home/ec2-user/loadBalancer

cd '/home/ec2-user/loadBalancer/LoadBalancer'

mvn -e clean install >> /var/log/loadBalancer.log
sudo rm -rf /usr/share/tomcat7/webapps/*
sudo cp target/*.war /usr/share/tomcat7/webapps/ >> /var/log/LoadBalancer.log
cd  /usr/share/tomcat7/bin

sudo service tomcat7 start >> /var/log/tomcat.log 2>&1 &

