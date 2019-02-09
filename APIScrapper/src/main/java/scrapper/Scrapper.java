package scrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Hashtable;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import model.Job;
import model.Organization;
import model.Person;
import model.Strength;

public class Scrapper {

	private final static String GET_PEOPLE_URL = "https://torre.bio/api/people";
	private final static String GET_BIO_URL = " https://torre.bio/api/bios/";

	private Hashtable<String, Person> hashPeople;
	private Hashtable<String, Job> hashJobs;
	private Hashtable<String, Strength> hashStrengths;
	private Hashtable<String, Organization> hashOrganizations;
	private ArrayDeque<String> peopleToVisit;
	private int maxPeopleSize = 1000;

	public Scrapper() {
		hashPeople = new Hashtable<String, Person>();
		hashJobs = new Hashtable<String, Job>();
		hashStrengths = new Hashtable<String, Strength>();
		hashOrganizations = new Hashtable<String, Organization>();
		peopleToVisit = new ArrayDeque<>();
		peopleToVisit.push("torrenegra");
		
		while(!peopleToVisit.isEmpty() && hashPeople.size() <= maxPeopleSize) {
			String nextPerson = peopleToVisit.pop();
			visitPerson(nextPerson);
		}

		try {
			String peopleResponse = sendGET(GET_PEOPLE_URL);
			if (peopleResponse != "ERR") {
				// Parse JSON
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void visitPerson(String personId) {
		
		String url = GET_BIO_URL+personId;
		try {
			String response = sendGET(url);
			if (response != "ERR") {
				JsonParser parser= new JsonParser();
				JsonElement element = parser.parse(response);
				JsonObject objectResponse = element.getAsJsonObject();
				JsonObject person = (JsonObject) objectResponse.get("person");
				JsonArray strengths = (JsonArray) objectResponse.get("strengths");
				JsonArray jobs = (JsonArray) objectResponse.get("jobs");
				
				String name = person.getAsJsonPrimitive("name").getAsString();
				String headline = person.getAsJsonPrimitive("professionalHeadline").getAsString();
				String picture = person.getAsJsonPrimitive("picture").getAsString();
				double weight = person.getAsJsonPrimitive("weight").getAsDouble();
				
				for (int i = 0; i < strengths.size(); i++) {
					JsonObject strength = (JsonObject) strengths.get(i);
					String code = strength.getAsJsonPrimitive("code").getAsString();
					String strengthName = strength.getAsJsonPrimitive("name").getAsString();
				}
				
				for (int i = 0; i < jobs.size(); i++) {
					JsonObject job = (JsonObject) jobs.get(i);
					System.out.println(job);
					String role = job.getAsJsonPrimitive("role").getAsString();
					String fromMonth = job.getAsJsonPrimitive("role").getAsString();
					String fromYear = job.getAsJsonPrimitive("role").getAsString();
					String toMonth = job.getAsJsonPrimitive("role").getAsString();
					String toYear = job.getAsJsonPrimitive("role").getAsString();
					
					JsonArray organizations = (JsonArray) job.get("organizations");
					
					for (int j = 0; j < organizations.size(); j++) {
						JsonObject organization = (JsonObject) organizations.get(j);
						int oid = organization.getAsJsonPrimitive("id").getAsInt();
						String oName = organization.getAsJsonPrimitive("name").getAsString();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	

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
		Scrapper mainClass = new Scrapper();
	}
}
