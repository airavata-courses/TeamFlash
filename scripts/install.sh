
echo 'Installing Registry' 
cd '/home/ec2-user/Registry/Registry'
mvn clean install >> /var/log/Registry.log
cp config.properties target/
cd 'target'
chmod 777 *
java -jar Registry*.jar 8800 >> /var/log/Registry1.log 2>&1 &
