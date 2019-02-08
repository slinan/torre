from flask import Flask, jsonify
from apscheduler.schedulers.background import BackgroundScheduler
from pymongo import MongoClient
import requests
import datetime
import os

app = Flask(__name__)

user = os.environ["DB_USER"]
password = os.environ["DB_PASSWORD"]
url = os.environ["DB_URL"]
port = os.environ["DB_PORT"]
dbName = os.environ["DB_NAME"]
connectionString = 'mongodb://'+user+':'+password+'@'+url+':'+port+'/'+dbName
client = MongoClient(connectionString)
db = client['performance']

class Slave:
    
    def __init__(self, url, apiUrl, zone):
        self.url = url
        self.apiUrl = apiUrl
        self.zone = zone

    def calculateAverage(self):
        data = {'url': self.apiUrl}
        req = requests.post(self.url, data=data)
        print(req.response)

def calculeteAverage():
    response = {slave1.calculateAverage(), slave2.calculateAverage(), slave3.calculateAverage(), slave4.calculateAverage()}
    return jsonify({'data':response})

@app.route("/")
def hello():
    return "Running Master"

@app.route("/average")
def average():
    return calculeteAverage()

apiUrl = os.environ["API_URL"]
slave1 = Slave(os.environ["SLAVE1_URL"], apiUrl, 'US. West Coast')
slave2 = Slave(os.environ["SLAVE2_URL"], apiUrl, 'US. East Coast')
slave3 = Slave(os.environ["SLAVE3_URL"], apiUrl, 'Asia')
slave4 = Slave(os.environ["SLAVE4_URL"], apiUrl, 'South America')

scheduler = BackgroundScheduler()
job = scheduler.add_job(calculeteAverage, 'interval', minutes=1)
scheduler.start()