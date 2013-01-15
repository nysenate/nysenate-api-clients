package gov.nysenate.services.model;

public class District {
	private int number;
	private String url;
	private String imageUrl;
	private String mapUrl;

	public District() {
	    this(0, "", "", "");
	}

	public District(int number, String url, String imageUrl, String mapUrl) {
		this.setNumber(number);
		this.setImageUrl(imageUrl);
		this.setUrl(url);
		this.setMapUrl(mapUrl);
	}

	public String getUrl() {
	    return url;
	}

	public int getNumber() {
		return number;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setUrl(String url) {
        this.url = url==null ? "" : url;
    }

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl==null ? "" : imageUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl==null ? "" : mapUrl;
	}
}
