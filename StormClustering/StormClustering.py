import os
from flask import Flask
from flask import Response
from flask import json
from flask import make_response
from flask import request
import requests

app = Flask(__name__)


@app.route('/detectClusters', methods=['GET'])
def detectClusters():
	try:
		id = request.args.get('id')
		if(id == null):
			requests.get("http://52.55.196.63:8888/login")
	except:
		pass
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


if __name__ == '__main__':
	port = int(os.environ.get('PORT', 8000))
	app.run(host='0.0.0.0', port=port)

