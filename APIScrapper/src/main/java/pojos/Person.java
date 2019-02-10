package pojos;

import java.util.ArrayList;

public class Person {
	
	private String image;
	private String name;
	private String header;
	private ArrayList<String> jobs;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public ArrayList<String> getJobs() {
		return jobs;
	}
	public void setJobs(ArrayList<String> jobs) {
		this.jobs = jobs;
	}

}
