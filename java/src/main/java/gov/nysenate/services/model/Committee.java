package gov.nysenate.services.model;

import java.util.ArrayList;

public class Committee implements Comparable<Committee>
{
    private Integer nid;
	private String name;
	private String shortName;
	private String url;
	private String videoUrl;
	private ArrayList<Member> chairs;
	private ArrayList<Member> members;

	public Committee()
	{
		this(0, "", "", "","");
	}

	public Committee(Integer nid, String name, String shortName, String url, String videoUrl)
	{
		this(nid, name, shortName, url, videoUrl, new ArrayList<Member>(), new ArrayList<Member>());
	}

	public Committee(Integer nid, String name, String shortName, String url, String videoUrl,
                ArrayList<Member> chairs, ArrayList<Member> members)
	{
	    this.setNid(nid);
		this.setName(name);
		this.setShortName(shortName);
		this.setUrl(url);
		this.setVideoUrl(videoUrl);
		this.setChairs(chairs);
		this.setMembers(members);
	}

	public String getName()
	{
		return name;
	}

	public String getShortName()
	{
		return shortName;
	}

	public String getUrl()
	{
		return url;
	}

	public String getVideoUrl()
	{
	    return videoUrl;
	}

	public ArrayList<Member> getChairs()
	{
		return chairs;
	}

	public ArrayList<Member> getMembers()
	{
		return members;
	}

	public void setName(String name)
	{
		this.name = name==null ? "" : name;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName==null ? "" : shortName;
	}

	public void setUrl(String url)
	{
		this.url = url==null ? "" : url;
	}

	public void setVideoUrl(String videoUrl)
	{
	    this.videoUrl = videoUrl==null ? "" : videoUrl;
	}

	public void setChairs(ArrayList<Member> chairs)
	{
		this.chairs = chairs==null ? new ArrayList<Member>() : chairs;
	}

	public void setMembers(ArrayList<Member> members)
	{
		this.members = members==null ? new ArrayList<Member>() : members;
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
    public int compareTo(Committee o)
    {
        return this.getNid().compareTo(o.getNid());
    }
}
