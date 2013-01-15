package gov.nysenate.services.model;

import java.util.ArrayList;

public class Committee {
	private String name;
	private String shortName;
	private String url;
	private ArrayList<Member> chairs;
	private ArrayList<Member> members;

	public Committee() {
		this("", "", "");
	}

	public Committee(String name, String shortName, String url) {
		this(name, shortName, url, new ArrayList<Member>(), new ArrayList<Member>());
	}

	public Committee(String name, String shortName, String url,
				ArrayList<Member> chairs,
				ArrayList<Member> members) {

		this.setName(name);
		this.setShortName(shortName);
		this.setUrl(url);
		this.setChairs(chairs);
		this.setMembers(members);
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public String getUrl() {
		return url;
	}

	public ArrayList<Member> getChairs() {
		return chairs;
	}

	public ArrayList<Member> getMembers() {
		return members;
	}

	public void setName(String name) {
		this.name = name==null ? "" : name;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName==null ? "" : shortName;
	}

	public void setUrl(String url) {
		this.url = url==null ? "" : url;
	}

	public void setChairs(ArrayList<Member> chairs) {
		this.chairs = chairs==null ? new ArrayList<Member>() : chairs;
	}

	public void setMembers(ArrayList<Member> members) {
		this.members = members==null ? new ArrayList<Member>() : members;
	}
}
