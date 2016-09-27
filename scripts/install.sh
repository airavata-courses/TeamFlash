echo 'Installing DataIngestor' 
cd '/home/ec2-user/DataIngestor'
mvn -e clean install >> /var/log/DataIngestor.log
java -jar target/*.jar 2345 >> /var/log/DataIngestor.log