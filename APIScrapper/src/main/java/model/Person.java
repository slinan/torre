package model;

import java.util.ArrayList;

public class Person {

	private String publicId;
	private String name;
	private String professionalheadline;
	private String picture;
	private double weight;
	private ArrayList<Strength> strengths;
	private ArrayList<Job> jobs;

	public Person(String publicId, String name, String professionalheadline, String picture, double weight,
			ArrayList<Strength> strengths, ArrayList<Job> jobs) {
		super();
		this.publicId = publicId;
		this.name = name;
		this.professionalheadline = professionalheadline;
		this.picture = picture;
		this.weight = weight;
		this.strengths = strengths;
		this.jobs = jobs;
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

	public void setStrengths(ArrayList<Strength> strengths) {
		this.strengths = strengths;
	}

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}

}
