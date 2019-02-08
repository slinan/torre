from flask import Flask, jsonify, request, Response
import requests
import os

app = Flask(__name__)

def calculeteAverage(url):
    time1 = requests.get(url).elapsed
    time2 = requests.get(url).elapsed
    average = (time1+time2)/2
    seconds = average.total_seconds()
    return jsonify({'average':seconds})

@app.route("/")
def hello():
    return "Running Slave"

@app.route("/average", methods=['POST'])
def average():
    data = request.get_json()
    if data and data['url']:
        return calculeteAverage(data['url'])
    return Response("{'error':'URL not provided'}", status=400, mimetype='application/json')