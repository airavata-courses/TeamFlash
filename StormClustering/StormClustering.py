from flask import Flask
from flask import make_response

app = Flask(__name__)

@app.route('/')
def hello_world():
    current_dummy_data = "Random KML file data.Will contain data generated from Strom Clustering later in kml format!"
    response = make_response(current_dummy_data)
    response.headers["Content-Disposition"] = "attachment; filename=data.kml"
    return response


if __name__ == '__main__':
    app.run()
