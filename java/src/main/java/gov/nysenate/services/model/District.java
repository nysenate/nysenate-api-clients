package gov.nysenate.services.model;

public class District implements Comparable<District>
{
	private Integer number;
	private String url;

	public District() {
	    this(0, "");
	}

	public District(Integer number, String url) {
		this.setNumber(number);
		this.setUrl(url);
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
    public int compareTo(District o) {
        return this.getNumber().compareTo(o.getNumber());
    }
}
