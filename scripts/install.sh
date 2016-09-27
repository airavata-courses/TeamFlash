echo 'starting installation process' >> /var/log/teamflash-gateway-install.log
cd '/home/ec2-user/Gateway/Gateway' >> /var/log/teamflash-gateway-install.log
echo 'Activating virtualenv for StormClustering Microservice' >> /var/log/teamflash-gateway-install.log
npm install mongodb >> /var/log/teamflash-gateway-install.log
npm link mongodb >> /var/log/teamflash-gateway-install.log
npm install node-uuid >> /var/log/teamflash-gateway-install.log
node index.js >> /var/log/teamflash-gateway-server.log 2>&1 &
mongod >> /var/log/teamflash-gateway-db.log 2>&1 &
