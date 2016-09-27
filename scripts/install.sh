echo 'Installing DataIngestor' 
cd '/home/ec2-user/DataIngestor'
mvn clean install >> /var/log/DataIngestor.log
chmod 777 target/*
java -jar target/*.jar 2345 >> /var/log/DataIngestor.log
