echo 'Installing DataIngestor' 
cd '/home/ec2-user/DataIngestor/DataIngestor'
mvn clean install >> /var/log/DataIngestor.log
cd 'target'
chmod 777 *
java -jar *.jar 2345 >> /var/log/DataIngestor.log
