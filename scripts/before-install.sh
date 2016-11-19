echo 'killing existing tomcat process if any'
cd /usr/share/tomcat7/bin
sudo service tomcat7 stop
sleep 20
echo 'Setting up our java environment'
#export JAVA_HOME=/usr/java/jdk1.8.0_45/jre
echo 'check if maven is installed'
mvn --version
if [ "$?" -ne 0 ]; then
    echo 'Installing Maven...'
	sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
	sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
	sudo yum install -y apache-maven
	mvn --version
fi
