package gov.nysenate.services.model;

public class Member implements Comparable<Member> {
    private Integer nid;
	private String name;
	private String shortName;
	private String url;

	public Member() {
	    this(0, "", "", "");
	}

	public Member(Integer nid, String name, String shortName, String url) {
	    this.setNid(nid);
		this.setName(name);
		this.setShortName(shortName);
		this.setUrl(url);
	}

	public Member(Senator senator) {
	    this(senator.getNid(), senator.getName(), senator.getShortName(), senator.getUrl());
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

    public Integer getNid()
    {
        return nid;
    }

    public void setNid(Integer nid)
    {
        this.nid = nid;
    }

    @Override
    public int compareTo(Member o)
    {
        return this.getNid().compareTo(o.getNid());
    }
}
