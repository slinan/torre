package scrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import org.bson.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import model.Job;
import model.Organization;
import model.Person;
import model.Strength;

public class Scrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static String GET_PEOPLE_URL = "https://torre.bio/api/people";
	private final static String GET_BIO_URL = " https://torre.bio/api/bios/";
	private final static String PEOPLE_URL_BASE = " https://torre.bio/api/people/";
	private final static String PEOPLE_URL_ENDING = "/connections?limit=100";

	private Hashtable<String, Person> hashPeople;
	private Hashtable<String, Job> hashJobs;
	private Hashtable<String, Strength> hashStrengths;
	private Hashtable<String, Organization> hashOrganizations;
	private ArrayDeque<String> peopleToVisit;
	private int visitedPeople = 0;
	private Hashtable<String, Boolean> hashPeopleToVisit;

	public Scrapper() {
		loadData();
		while (!peopleToVisit.isEmpty()) {

			String nextPerson = peopleToVisit.pop();
			while (hashPeople.get(nextPerson) != null) {
				nextPerson = peopleToVisit.pop();
			}
			visitPerson(nextPerson);
			loadPersonFriends(nextPerson);
			visitedPeople++;
			System.out.println(visitedPeople);
		}
		saveData();
	}

	private void loadPersonFriends(String person) {
		String url = PEOPLE_URL_BASE + person + PEOPLE_URL_ENDING;
		try {
			String peopleResponse = sendGET(url);
			if (peopleResponse != "ERR") {
				JsonParser parser = new JsonParser();
				JsonArray array = (JsonArray) parser.parse(peopleResponse);
				for (int i = 0; i < array.size(); i++) {
					JsonObject currentPerson = (JsonObject) array.get(i);
					JsonObject data = currentPerson.getAsJsonObject("person");
					String publicId = data.getAsJsonPrimitive("publicId").getAsString();
					if (hashPeopleToVisit.get(publicId) == null && hashPeople.get(publicId) == null) {
						peopleToVisit.push(publicId);
						hashPeopleToVisit.put(publicId, true);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void visitPerson(String personId) {
		System.out.println("Visiting "+personId);
		String url = GET_BIO_URL + personId;
		try {
			String response = sendGET(url);
			if (response != "ERR") {
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(response);
				JsonObject objectResponse = element.getAsJsonObject();
				JsonObject person = (JsonObject) objectResponse.get("person");
				JsonArray strengths = (JsonArray) objectResponse.get("strengths");
				JsonArray jobs = (JsonArray) objectResponse.get("jobs");

				String name = person.getAsJsonPrimitive("name").getAsString();
				String headline = person.getAsJsonPrimitive("professionalHeadline").getAsString();
				String picture = "";
				if (person.getAsJsonPrimitive("picture") != null) {
					picture = person.getAsJsonPrimitive("picture").getAsString();
				}
				double weight = person.getAsJsonPrimitive("weight").getAsDouble();
				Person newPerson = new Person(personId, name, headline, picture, weight);
				hashPeople.put(newPerson.getPublicId(), newPerson);
				for (int i = 0; i < strengths.size(); i++) {
					JsonObject strength = (JsonObject) strengths.get(i);
					String code = strength.getAsJsonPrimitive("code").getAsString();
					String strengthName = strength.getAsJsonPrimitive("name").getAsString();
					Strength newStrength = new Strength(code, strengthName);
					newPerson.addStrenght(newStrength);
					newStrength.addPerson(newPerson);
					if (hashStrengths.get(strengthName) == null) {
						hashStrengths.put(strengthName, newStrength);
					}
					Strength existing = hashStrengths.get(strengthName);
					existing.addPerson(newPerson);
					newPerson.addStrenght(existing);
				}

				if (jobs != null) {
					for (int i = 0; i < jobs.size(); i++) {
						String roleString = "";
						String fromMonthString = "";
						String fromYearString = "";
						String toMonthString = "";
						String toYearString = "";
						JsonObject job = (JsonObject) jobs.get(i);
						JsonPrimitive role = job.getAsJsonPrimitive("role");
						if (role != null) {
							roleString = role.getAsString();
						}
						JsonPrimitive fromMonth = job.getAsJsonPrimitive("fromMonth");
						if (fromMonth != null) {
							fromMonthString = fromMonth.getAsString();
						}
						JsonPrimitive fromYear = job.getAsJsonPrimitive("fromYear");
						if (fromYear != null) {
							fromYearString = fromYear.getAsString();
						}
						JsonPrimitive toMonth = job.getAsJsonPrimitive("toMonth");
						if (toMonth != null) {
							toMonthString = toMonth.getAsString();
						}
						JsonPrimitive toYear = job.getAsJsonPrimitive("toYear");
						if (toYear != null) {
							toYearString = toYear.getAsString();
						}

						JsonArray organizations = (JsonArray) job.get("organizations");

						Job newJob = new Job(roleString, fromMonthString, fromYearString, toMonthString, toYearString);
						newJob.addPerson(newPerson);
						newPerson.addJob(newJob);
						for (int j = 0; j < organizations.size(); j++) {
							JsonObject organization = (JsonObject) organizations.get(j);
							int oid = organization.getAsJsonPrimitive("id").getAsInt();
							String oName = organization.getAsJsonPrimitive("name").getAsString();
							Organization newOrganization = new Organization(oid, oName);

							if (hashOrganizations.get(oName) == null) {
								hashOrganizations.put(oName, newOrganization);
							}
							Organization existingOrg = hashOrganizations.get(oName);

							newJob.setOrganization(existingOrg);
							existingOrg.addJob(newJob);
						}
						if (hashJobs.get(roleString) == null) {
							hashJobs.put(roleString, newJob);
						}
						else {
							System.out.println("JOB MORE PEOPLE: "+newJob.getRole());
						}
						hashJobs.get(roleString).addPerson(newPerson);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveData() {
		
		String dbURL = System.getenv("DB_URL");
		MongoClient mongoClient = MongoClients.create(dbURL);
		MongoDatabase database = mongoClient.getDatabase("scrapper");
		MongoCollection<Document> jobs = database.getCollection("jobs");
		jobs.createIndex(Indexes.ascending("role"));
		
		ArrayList<Document> jobsList = new ArrayList<Document>();
		Enumeration<String> enumKey = hashJobs.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    Job job = hashJobs.get(key);
		    Document doc = new Document("role", key);
		    doc.append("people", job.getArrayIdPeople());
		    jobsList.add(doc);
		}
		
		jobs.insertMany(jobsList);
		
		MongoCollection<Document> people = database.getCollection("people");
		jobs.createIndex(Indexes.ascending("publicId"));
		Enumeration<String> enumPeople = hashPeople.keys();
		ArrayList<Document> peopleToInsert = new ArrayList<Document>();

		while(enumPeople.hasMoreElements()) {
		    String key = enumPeople.nextElement();
		    Person p = hashPeople.get(key);
		    Document doc = new Document("publicId", key)
		    	.append("jobs", p.getJobsStrings())
		    	.append("headline",p.getProfessionalheadline())
		    	.append("strengths",p.getStrengthsList())
		    	.append("photo",p.getPicture())
		    	.append("name",p.getName())
		    	.append("weight",p.getWeight());
		    peopleToInsert.add(doc);
		}
		people.insertMany(peopleToInsert);
		
	}

	private void loadData() {
			hashPeople = new Hashtable<String, Person>();
			hashJobs = new Hashtable<String, Job>();
			hashStrengths = new Hashtable<String, Strength>();
			hashOrganizations = new Hashtable<String, Organization>();
			peopleToVisit = new ArrayDeque<>();
			peopleToVisit.push("torrenegra");
			hashPeopleToVisit = new Hashtable<String, Boolean>();
	}

	@SuppressWarnings("unchecked")
	private String sendGET(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return response.toString();

		} else {
			System.out.println("HTTP Error");
		}

		return "ERR";
	}

	public static void main(String[] args) {
		//Scrapper mainClass = new Scrapper();
	}
}
