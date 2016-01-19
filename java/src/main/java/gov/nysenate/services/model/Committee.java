package gov.nysenate.services.model;

import java.util.List;

public class Committee implements Comparable<Committee>
{
    private Integer nid;
	private String name;
	private String shortName;
	private String url;
	private String videoUrl;
	private List<Senator> chairs;
	private List<Senator> members;

	public Committee() {}

	public Integer getNid() {
		return nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public List<Senator> getChairs() {
		return chairs;
	}

	public void setChairs(List<Senator> chairs) {
		this.chairs = chairs;
	}

	public List<Senator> getMembers() {
		return members;
	}

	public void setMembers(List<Senator> members) {
		this.members = members;
	}

	@Override
    public int compareTo(Committee o)
    {
        return this.getNid().compareTo(o.getNid());
    }
}