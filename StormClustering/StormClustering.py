import os
from flask import Response
from flask import json
from flask import Flask
import json
import requests
import uuid
from kazoo.client import KazooClient
from kazoo.client import KazooState
from kazoo.exceptions import KazooException
import logging
import datetime
from datetime import datetime

app = Flask(__name__)


@app.route('/detectClusters', methods=['GET'])
def detectClusters():
	
	clusters = {
		
        "data" : [
            {"longitude":"-112.0822680013139","latitude":"36.09825589333556","altitude":"0",
             "heading":"103.8120432044965","tilt":"62.04855796276328","range":"2889.145007690472"},
            {"longitude": "-112.0822680013139", "latitude": "36.09825589333556", "altitude": "0",
             "heading": "103.8120432044965", "tilt": "62.04855796276328", "range": "2889.145007690472"},
            {"longitude": "-112.0822680013139", "latitude": "36.09825589333556", "altitude": "0",
             "heading": "103.8120432044965", "tilt": "62.04855796276328", "range": "2889.145007690472"}
        ]
        
    }

	js = json.dumps(clusters)
	response = Response(js, status=200, mimetype='application/json')
	return response


def register():
	try:
		global ip
		id = str(uuid.uuid4())
		zookeeperIP = "52.52.144.190:2181"
		#zookeeperIP = "localhost:2181"
		zk = KazooClient(hosts=zookeeperIP)
		zk.start()
		zk.add_listener(my_listener)
		path = "http://" + ip + ":" + str(port) + "/detectClusters"
		zk.create("/StormClustering/worker/" + id, json.dumps(
			{'name': 'stormClustering', 'address': ip, 'port': str(port), 'sslPort': None, 'payload': None,
			 'registrationTimeUTC': (datetime.utcnow() - datetime.utcfromtimestamp(0)).total_seconds(),
			 'serviceType': 'DYNAMIC', "uriSpec": {"parts": [{"value": path, "variable": False}]}},
			ensure_ascii=True).encode(), ephemeral=True, makepath=True)
	except KazooException as e:
		print e.__doc__
		print "error "+e.message
	logging.basicConfig()


def tearDown():
	global zk
	zk.stop()


def my_listener(state):
	global ip
	if state == KazooState.LOST:
		zookeeperIP = "52.52.144.190:2181"
		#zookeeperIP = "localhost:2181"
		zk = KazooClient(hosts=zookeeperIP)
		zk.start()
	elif state == KazooState.SUSPENDED:
		print "connection suspended"
	else:
		print "connection error"


if __name__ == '__main__':
	port = int(os.environ.get('PORT', 8000))
	ip = requests.get("http://checkip.amazonaws.com/").text.split("\n")[0]
	#ip = "localhost"
	register()
	app.run(host='0.0.0.0', port=port)

