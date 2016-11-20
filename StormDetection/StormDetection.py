from flask import Flask
from flask import make_response
from flask import request
from flask import json
import json
import requests
import uuid
from kazoo.client import KazooClient
from kazoo.client import KazooState
from kazoo.exceptions import KazooException
import logging
import datetime
from datetime import datetime

#from urllib.request import urlopen
import os

app = Flask(__name__)

@app.route('/detectStorm', methods=['GET','POST'])
def detectStormLocation():
    #url = 'https://noaa-nexrad-level2.s3.amazonaws.com/' + year + '/' + month + '/' + day + '/' + station + '/' + filename + '.gz'
    url = request.args.get('url')
    try:
        data = requests.get(url).read()
        #data = urlopen(url).read()
    except:
        pass
    script_dir = os.path.dirname(__file__)
    rel_path = "dummy.kml"
    abs_file_path = os.path.join(script_dir, rel_path)
    content_data = open(abs_file_path,'r').read()
    response = make_response(content_data)
    response.headers["Content-Disposition"] = "attachment; filename=data.kml"
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
        path = "http://" + ip + ":" + str(port) + "/detectStorm"
        zk.create("/StormDetection/worker/" + id, json.dumps(
			{'name': 'stormDetection', 'address': ip, 'port': str(port), 'sslPort': None, 'payload': None,
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
    port = int(os.environ.get('PORT', 7000))
    ip = requests.get("http://checkip.amazonaws.com/").text.split("\n")[0]
    #ip = "localhost"
    register()
    app.run(host='0.0.0.0', port=port)
