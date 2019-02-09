package scrapper;

import java.io.BufferedReader;
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
import java.util.Hashtable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Job;
import model.Organization;
import model.Person;
import model.Strength;

public class Scrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	private final static String GET_PEOPLE_URL = "https://torre.bio/api/people";
	private final static String GET_BIO_URL = " https://torre.bio/api/bios/";

	private Hashtable<String, Person> hashPeople;
	private Hashtable<String, Job> hashJobs;
	private Hashtable<String, Strength> hashStrengths;
	private Hashtable<String, Organization> hashOrganizations;
	private ArrayDeque<String> peopleToVisit;
	private int visitedPeople = 0;

	public Scrapper() {
		
		while(!peopleToVisit.isEmpty()) {
			
			if(visitedPeople % 50 == 0) {
				saveData();
			}
			
			String nextPerson = peopleToVisit.pop();
			visitPerson(nextPerson);
			loadPersonFriends(nextPerson);
			visitedPeople++;
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
				Person newPerson = new Person(personId, name, headline, picture, weight);
				for (int i = 0; i < strengths.size(); i++) {
					JsonObject strength = (JsonObject) strengths.get(i);
					String code = strength.getAsJsonPrimitive("code").getAsString();
					String strengthName = strength.getAsJsonPrimitive("name").getAsString();
					Strength newStrength = new Strength(code, strengthName);
					newPerson.addStrenght(newStrength);
					newStrength.addPerson(newPerson);
					if(hashStrengths.get(strengthName) == null)
					{
						hashStrengths.put(strengthName, newStrength);
					}
					Strength existing = hashStrengths.get(strengthName);
					existing.addPerson(newPerson);
					newPerson.addStrenght(existing);
				}
				
				for (int i = 0; i < jobs.size(); i++) {
					JsonObject job = (JsonObject) jobs.get(i);
					String role = job.getAsJsonPrimitive("role").getAsString();
					String fromMonth = job.getAsJsonPrimitive("fromMonth").getAsString();
					String fromYear = job.getAsJsonPrimitive("fromYear").getAsString();
					String toMonth = job.getAsJsonPrimitive("toMonth").getAsString();
					String toYear = job.getAsJsonPrimitive("toYear").getAsString();
					JsonArray organizations = (JsonArray) job.get("organizations");
					
					Job newJob = new Job(role, fromMonth, fromYear, toMonth, toYear);
					newJob.addPerson(newPerson);
					newPerson.addJob(newJob);
					for (int j = 0; j < organizations.size(); j++) {
						JsonObject organization = (JsonObject) organizations.get(j);
						int oid = organization.getAsJsonPrimitive("id").getAsInt();
						String oName = organization.getAsJsonPrimitive("name").getAsString();
						Organization newOrganization = new Organization(oid, oName);
						
						if(hashOrganizations.get(oName) == null)
						{
							hashOrganizations.put(oName, newOrganization);
						}
						Organization existingOrg = hashOrganizations.get(oName);

						hashJobs.get(role).addPerson(newPerson);
						
						newJob.setOrganization(existingOrg);
						existingOrg.addJob(newJob);
					}
					if(hashJobs.get(role) == null)
					{
						hashJobs.put(role, newJob);
					}
					hashJobs.get(role).addPerson(newPerson);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveData() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("people.tmp");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(hashPeople);
			oos.close();

			fos = new FileOutputStream("jobs.tmp");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(hashJobs);
			oos.close();

			fos = new FileOutputStream("strengths.tmp");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(hashStrengths);
			oos.close();

			fos = new FileOutputStream("organizations.tmp");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(hashOrganizations);
			oos.close();

			fos = new FileOutputStream("peopleToVisit.tmp");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(peopleToVisit);
			oos.close();

			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadData() {
		boolean loadNewData = false;
        try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("people.tmp"));
			hashPeople = (Hashtable<String, Person>) ois.readObject();
			ois.close();
			
			ois = new ObjectInputStream(new FileInputStream("jobs.tmp"));
			hashJobs = (Hashtable<String, Job>) ois.readObject();
			ois.close();
			
			ois = new ObjectInputStream(new FileInputStream("strengths.tmp"));
			hashStrengths = (Hashtable<String, Strength>) ois.readObject();
			ois.close();
			
			ois = new ObjectInputStream(new FileInputStream("organizations.tmp"));
			hashOrganizations = (Hashtable<String, Organization>) ois.readObject();
			ois.close();
			
			ois = new ObjectInputStream(new FileInputStream("peopleToVisit.tmp"));
			peopleToVisit = (ArrayDeque<String>) ois.readObject();
			ois.close();

		} catch (FileNotFoundException e) {
			loadNewData = true;
		} catch (IOException e) {
			e.printStackTrace();
			loadNewData = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			loadNewData = true;
		}

        if(loadNewData) {
		hashPeople = new Hashtable<String, Person>();
		hashJobs = new Hashtable<String, Job>();
		hashStrengths = new Hashtable<String, Strength>();
		hashOrganizations = new Hashtable<String, Organization>();
		peopleToVisit = new ArrayDeque<>();
		peopleToVisit.push("torrenegra");
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
