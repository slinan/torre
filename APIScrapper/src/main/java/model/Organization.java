package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Organization implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private ArrayList<Job> jobs;
	public Organization(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		jobs = new ArrayList<Job>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addJob(Job newJob) {
		jobs.add(newJob);
		
	}
	
	

}
