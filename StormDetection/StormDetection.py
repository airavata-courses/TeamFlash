from flask import Flask
from flask import make_response
from flask import request
import requests
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


if __name__ == '__main__':
    port = int(os.environ.get('PORT', 7000))
    app.run(host='0.0.0.0', port=port)
