from flask import Flask
from flask import make_response
from urllib.request import urlopen

app = Flask(__name__)

@app.route('/detectStorm/<year>/<month>/<day>/<station>/<filename>', methods=['GET'])
def detectStormLocation(year=None, month=None, day=None, station=None, filename=None):
    url = 'https://noaa-nexrad-level2.s3.amazonaws.com/' + year + '/' + month + '/' + day + '/' + station + '/' + filename + '.gz'
    #url = 'https://noaa-nexrad-level2.s3.amazonaws.com/1999/04/05/KAMX/KAMX19990405_000408.gz'
    data = urlopen(url).read()
    response = make_response(data)
    response.headers["Content-Disposition"] = "attachment; filename=data.kml"
    return response


if __name__ == '__main__':
    app.run()
