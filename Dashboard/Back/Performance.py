from flask import Flask, jsonify
from apscheduler.schedulers.background import BackgroundScheduler
from pymongo import MongoClient
import requests
import datetime
import os

app = Flask(__name__)

def calculeteAverage():
    url = os.environ["API_URL"]
    time1 = requests.get(url).elapsed
    time2 = requests.get(url).elapsed
    time3 = requests.get(url).elapsed
    print(type(time3))
    average = (time1+time2+time3)/3
    seconds = average.total_seconds()
    return jsonify({'average':seconds})

@app.route("/")
def hello():
    connectToMongo()
    return "Running"

@app.route("/average")
def average():
    return calculeteAverage()


def test_job():
    return "ok"

def connectToMongo():
    user = os.environ["DB_USER"]
    password = os.environ["DB_PASSWORD"]
    url = os.environ["DB_URL"]
    port = os.environ["DB_PORT"]
    dbName = os.environ["DB_NAME"]
    connectionString = 'mongodb://'+user+':'+password+'@'+url+':'+port+'/'+dbName
    client = MongoClient(connectionString)
    db = client['performance']
    post = {"author": "Mike",
        "text": "My first blog post!",
        "tags": ["mongodb", "python", "pymongo"],
        "date": datetime.datetime.utcnow()}
    posts = db.posts
    post_id = posts.insert_one(post).inserted_id
    print('Done')

connectToMongo()
scheduler = BackgroundScheduler()
job = scheduler.add_job(test_job, 'interval', minutes=1)
scheduler.start()