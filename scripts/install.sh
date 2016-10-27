echo 'starting installation process' >> /var/log/teamflash-gateway-install.log
rm -r /home/ec2-user/gateway
mv /home/ec2-user/Gateway  /home/ec2-user/gateway

cd /home/ec2-user/gateway/
chmod 777 gateway
cd Gateway

npm install mongodb >> /var/log/teamflash-gateway-install.log
npm install node-uuid >> /var/log/teamflash-gateway-install.log
npm link mongodb >> /var/log/teamflash-gateway-install.log
node index.js >> /var/log/teamflash-gateway-server.log 2>&1 &
mongod >> /var/log/teamflash-gateway-db.log 2>&1 &
