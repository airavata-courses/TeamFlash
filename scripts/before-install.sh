
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
#sudo pkill -f 'DataIngestor-1.0-SNAPSHOT.jar'
docker ps -a | grep 'DataIngestor' | awk '{print $1}' | xargs --no-run-if-empty docker stop >> /var/log/flask-before-clustering.log
docker ps -a | grep 'DataIngestor' | awk '{print $1}' | xargs --no-run-if-empty docker rm >> /var/log/flask-before-clustering.log