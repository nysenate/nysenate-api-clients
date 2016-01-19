package gov.nysenate.services.model;

import java.util.List;

public class Senator implements Comparable<Senator>
{
	private String name;
	private String firstName;
	private String lastName;
	private String shortName;
	private String email;
	private String additionalContact;
	private String imageUrl;
	private String url;
	private String summary;
	private List<String> partyAffiliations;
	private List<Office> offices;
	private District district;
	private Social social;
	private Integer openLegId;

	public Senator() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdditionalContact() {
		return additionalContact;
	}

	public void setAdditionalContact(String additionalContact) {
		this.additionalContact = additionalContact;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getPartyAffiliations() {
		return partyAffiliations;
	}

	public void setPartyAffiliations(List<String> partyAffiliations) {
		this.partyAffiliations = partyAffiliations;
	}

	public List<Office> getOffices() {
		return offices;
	}

	public void setOffices(List<Office> offices) {
		this.offices = offices;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Social getSocial() {
		return social;
	}

	public void setSocial(Social social) {
		this.social = social;
	}

	public Integer getOpenLegId() {
		return openLegId;
	}

	public void setOpenLegId(Integer openLegId) {
		this.openLegId = openLegId;
	}

	@Override
    public int compareTo(Senator o) {
        return this.shortName.compareTo(o.shortName);
    }
}