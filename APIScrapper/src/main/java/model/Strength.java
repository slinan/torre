package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Strength implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private ArrayList<Person> people;

	public Strength(String code, String name) {
		super();
		this.code = code;
		this.name = name;
		people = new ArrayList<Person>();
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void addPerson(Person newPerson) {
		people.add(newPerson);
	}

}
