
echo 'Installing Registry'

rm -r /home/ec2-user/registry
mv /home/ec2-user/Registry  /home/ec2-user/registry

cd /home/ec2-user/registry/
cd Registry

sudo mvn clean install >> /var/log/Registry.log
cp config.properties target/
sudo rm -rf /var/log/Registry1.log
cd 'target'
chmod 777 *
java -jar Registry*.jar 8550 >> /var/log/Registry1.log 2>&1 &
