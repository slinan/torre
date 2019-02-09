package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Job implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String role;
	private Organization organization;
	private String fromMonth;
	private String fromYear;
	private String toMonth;
	private String toYear;
	private ArrayList<Person> peopleWithThisJob;
	private Hashtable<String, Boolean> hashPeople;

	public Job(String role, String fromMonth, String fromYear, String toMonth, String toYear) {
		super();
		this.role = role;
		this.fromMonth = fromMonth;
		this.fromYear = fromYear;
		this.toMonth = toMonth;
		this.toYear = toYear;
		peopleWithThisJob = new ArrayList<Person>();
		hashPeople = new Hashtable<>();
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}

	public String getFromYear() {
		return fromYear;
	}

	public void setFromYear(String fromYear) {
		this.fromYear = fromYear;
	}

	public String getToMonth() {
		return toMonth;
	}

	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}

	public String getToYear() {
		return toYear;
	}

	public void setToYear(String toYear) {
		this.toYear = toYear;
	}

	public boolean addPerson(Person person) {
		if (hashPeople.get(person.getPublicId()) == null) {
			peopleWithThisJob.add(person);
			hashPeople.put(person.getPublicId(), true);
			if(peopleWithThisJob.size() > 1) {
				System.out.println("Job with more than one");
			}
			return true;
		}
		return false;
	}

	public ArrayList<Person> getPeopleWithThisJob() {
		return peopleWithThisJob;
	}

	public ArrayList<String> getArrayIdPeople() {
		ArrayList<String> people = new ArrayList<>();
		peopleWithThisJob.forEach(p -> {
			people.add(p.getPublicId());
		});
		return people;
	}
}
