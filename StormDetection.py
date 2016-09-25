from flask import Flask
from flask import make_response
from urllib.request import urlopen
import os

app = Flask(__name__)

@app.route('/detectStorm/<year>/<month>/<day>/<station>/<filename>', methods=['GET'])
def detectStormLocation(year=None, month=None, day=None, station=None, filename=None):
    url = 'https://noaa-nexrad-level2.s3.amazonaws.com/' + year + '/' + month + '/' + day + '/' + station + '/' + filename + '.gz'
    data = urlopen(url).read()
    script_dir = os.path.dirname(__file__)
    rel_path = "dummy.kml"
    abs_file_path = os.path.join(script_dir, rel_path)
    content_data = open(abs_file_path,'r').read()
    response = make_response(content_data)
    response.headers["Content-Disposition"] = "attachment; filename=data.kml"
    return response


if __name__ == '__main__':
    app.run()
