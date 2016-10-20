echo 'Installing DataIngestor' 
cd '/home/ec2-user/DataIngestor/DataIngestor'
sudo rm -rf target
mvn clean install >> /var/log/DataIngestor.log
#cd 'target'
#chmod 777 *
#java -jar Data*.jar 8765 >> /var/log/DataIngestor.log 2>&1 &
docker build -t dataingestor .
docker run -p 8765:8765 --name DataIngestor dataingestor >> /var/log/DataIngestor.log 2>&1 &
