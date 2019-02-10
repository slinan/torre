package pojos;

import java.util.ArrayList;

public class Job {
	
	private String role;
	private String dbId;
	private ArrayList<String> users;

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	public ArrayList<String> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}

}
