echo 'Installing DataIngestor' 
cd '/home/ec2-user/DataIngestor/DataIngestor'


rm -r /home/ec2-user/dataIngestor
mv /home/ec2-user/DataIngestor  /home/ec2-user/dataIngestor

cd /home/ec2-user/dataIngestor/

cd DataIngestor

sudo rm -rf target
mvn clean install >> /var/log/DataIngestor.log
#cd 'target'
#chmod 777 *
#java -jar Data*.jar 8765 >> /var/log/DataIngestor.log 2>&1 &
docker build -t dataingestor .
docker run -p 8765:8765 --name DataIngestor dataingestor >> /var/log/DataIngestor.log 2>&1 &
