package model;

public class Job {

	private String role;
	private Organization organization;
	private String fromMonth;
	private String fromYear;
	private String toMonth;
	private String toYear;

	public Job(String role, Organization organization, String fromMonth, String fromYear, String toMonth,
			String toYear) {
		super();
		this.role = role;
		this.organization = organization;
		this.fromMonth = fromMonth;
		this.fromYear = fromYear;
		this.toMonth = toMonth;
		this.toYear = toYear;
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

}
