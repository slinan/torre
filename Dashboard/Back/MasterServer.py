from flask import Flask, jsonify
from apscheduler.schedulers.background import BackgroundScheduler
from pymongo import MongoClient
import requests
import datetime
import os
import json
import time
from bson.json_util import dumps
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

user = os.environ["DB_USER"]
password = os.environ["DB_PASSWORD"]
url = os.environ["DB_URL"]
port = os.environ["DB_PORT"]
dbName = os.environ["DB_NAME"]
connectionString = 'mongodb://'+user+':'+password+'@'+url+':'+port+'/'+dbName
print(connectionString)
client = MongoClient(connectionString)
db = client['performance']

class Slave:
    
    def __init__(self, url, apiUrl, zone):
        self.url = url
        self.apiUrl = apiUrl
        self.zone = zone

    def calculateAverage(self):
        data = {'url': self.apiUrl}
        req = requests.post(self.url, json=data)
        avg = req.json()['average']
        return avg

def calculeteAverage():
    response = {'s1':slave1.calculateAverage(), 
    's2':slave2.calculateAverage(), 
    's3':slave3.calculateAverage(), 
    's4':slave4.calculateAverage()}
    print(response)
    return jsonify({'data':response})

def updateDatabase():
    now = datetime.datetime.utcnow()
    average = {'s1':slave1.calculateAverage(), 
    's2':slave2.calculateAverage(), 
    's3':slave3.calculateAverage(), 
    's4':slave4.calculateAverage(),
    'year': now.year,
    'day': now.day,
    'month': now.month,
    'hour': now.hour,
    'minute': now.minute,
    'date': now
    }
    insert = db.hours.insert_one(average)
    return 'OK'

def getLastRecords():
    size = db.hours.count()
    if size < 24:
        size = 0
    else:
        size = size - 24
    response = db.hours.find().skip(size)
    return dumps(response)

@app.route("/")
def hello():
    return "Running Master"

@app.route("/average")
def average():
    return calculeteAverage()

@app.route("/graphData")
def graphData():
    return calculeteAverage()

@app.route("/sendDB")
def sendDB():
    return updateDatabase()

@app.route("/lastReadings")
def lastRecs():
    return getLastRecords() 

apiUrl = os.environ["API_URL"]
slave1 = Slave(os.environ["SLAVE1_URL"], apiUrl, 'Virginia')
slave2 = Slave(os.environ["SLAVE2_URL"], apiUrl, 'California')
slave3 = Slave(os.environ["SLAVE3_URL"], apiUrl, 'London')
slave4 = Slave(os.environ["SLAVE4_URL"], apiUrl, 'Tokio')
updateDatabase()
scheduler = BackgroundScheduler()
job = scheduler.add_job(calculeteAverage, 'interval', minutes=30)
scheduler.start()