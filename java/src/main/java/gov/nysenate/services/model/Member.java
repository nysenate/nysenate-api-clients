package gov.nysenate.services.model;

public class Member {
	private String name;
	private String shortName;
	private String url;

	public Member() {
	    this("", "", "");
	}

	public Member(String name, String shortName, String url) {
		this.setName(name);
		this.setShortName(shortName);
		this.setUrl(url);
	}

	public Member(Senator senator) {
	    this(senator.getName(), senator.getShortName(), senator.getUrl());
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

	public void setName(String name) {
		this.name = name==null ? "" : name;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName==null ? "" : shortName;
	}

	public void setUrl(String url) {
		this.url = url==null ? "" : url;
	}
}
