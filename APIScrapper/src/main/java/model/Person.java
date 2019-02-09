package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Person implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String publicId;
	private String name;
	private String professionalheadline;
	private String picture;
	private double weight;
	private ArrayList<Strength> strengths;
	private Hashtable<String, Strength> hashStrengths;
	private ArrayList<Job> jobs;

	public Person(String publicId, String name, String professionalheadline, String picture, double weight) {
		super();
		this.publicId = publicId;
		this.name = name;
		this.professionalheadline = professionalheadline;
		this.picture = picture;
		this.weight = weight;
		this.strengths = new ArrayList<Strength>();
		this.jobs =  new ArrayList<Job>();
		hashStrengths = new Hashtable<>();
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfessionalheadline() {
		return professionalheadline;
	}

	public void setProfessionalheadline(String professionalheadline) {
		this.professionalheadline = professionalheadline;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public ArrayList<Strength> getStrengths() {
		return strengths;
	}

	public void addStrenght(Strength strength) {
		if (hashStrengths.get(strength.getName()) == null) {
			strengths.add(strength);
			hashStrengths.put(strength.getName(), strength);
		}

	}

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void addJob(Job job) {
		jobs.add(job);
	}
	
	public ArrayList<String> getJobsStrings() {
		ArrayList<String> answer = new ArrayList();
		jobs.forEach(job -> {
			String ans = job.getRole();
			if(job.getOrganization() != null) {
				ans += ": "+job.getOrganization().getName();
			}
			answer.add(ans);

		});
		return answer;
	}

	public ArrayList<String> getStrengthsList() {
		ArrayList<String> answer = new ArrayList();
		strengths.forEach(s -> {
			answer.add(s.getName());
		});
		return answer;
	}

}
