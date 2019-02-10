package webserver;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.internal.connection.tlschannel.NeedsReadException;

import pojos.Job;
import pojos.Person;
import spark.Request;
import spark.Response;
import spark.Spark;
import util.JsonHelper;

public class Main {
	public static void main(String[] args) {

		Spark.exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});
		port(8080);
		path("/api", () -> {
			before((request, response) -> response.type("application/json"));
			before("/*", (q, a) -> System.out.println("Received api call"));
			get("/paths", (req, res) -> getAllPaths(req, res));
			get("/jobs", (req, res) -> getAllJobs(req, res));
			after((req, res) -> res.type("application/json"));
		});

	}

	@SuppressWarnings("deprecation")
	public static String getAllJobs(Request req, Response res) {
		res.header("Access-Control-Allow-Origin", "*");
		res.header("Access-Control-Allow-Methods", "GET");
		res.type("application/json");
		String dbURL = System.getenv("DB_URL");
		MongoClient mongoClient = MongoClients.create(dbURL);
		MongoDatabase database = mongoClient.getDatabase("scrapper");
		MongoCollection<Document> jobs = database.getCollection("jobs");
		FindIterable<Document> allJobs = (FindIterable<Document>) jobs.find();
		Iterator iter = allJobs.iterator();
		ArrayList<Job> jobsResponse = new ArrayList<>();
		while (iter.hasNext()) {
			Document current = (Document) iter.next();
			Job nJob = new Job();
			nJob.setRole((String) current.get("role"));
			String id = current.get("_id").toString();
			nJob.setDbId(id);
			ArrayList<String> people = (ArrayList<String>) current.get("people");
			nJob.setUsers(people);
			jobsResponse.add(nJob);
		}
		return JsonHelper.dataToJson(jobsResponse);
	}

	@SuppressWarnings("deprecation")
	public static String getAllPaths(Request req, Response res) {
		res.header("Access-Control-Allow-Origin", "*");
		res.header("Access-Control-Allow-Methods", "GET");
		res.type("application/json");
		String oid = req.queryParams("dbId");
		if (oid == null) {
			return "No parameter";
		}
		String dbURL = System.getenv("DB_URL");
		MongoClient mongoClient = MongoClients.create(dbURL);
		MongoDatabase database = mongoClient.getDatabase("scrapper");
		MongoCollection<Document> jobsCollection = database.getCollection("jobs");
		MongoCollection<Document> peopleCollection = database.getCollection("people");
		Document job = jobsCollection.find(Filters.eq("_id", new ObjectId(oid))).first();
		ArrayList<String> people = (ArrayList<String>) job.get("people");

		ArrayList<Person> answer = new ArrayList<>();
		Person newPerson = new Person();
		people.forEach(p -> {
			System.out.println(p);
			Document person = peopleCollection.find(Filters.eq("publicId", p)).first();
			System.out.println(person);
			String name = person.getString("name");
			String image = person.getString("photo");
			String header = person.getString("headline");
			ArrayList<String> jobs = (ArrayList<String>) person.get("jobs");
			Person webPerson = new Person();
			webPerson.setHeader(header);
			webPerson.setImage(image);
			webPerson.setJobs(jobs);
			webPerson.setName(name);
			answer.add(webPerson);
		});
		return JsonHelper.dataToJson(answer);

	}
}
