package gov.nysenate.services.model;

import java.util.ArrayList;

public class Senator implements Comparable<Senator>
{
    private Integer nid;
	private String name;
	private String lastName;
	private String shortName;

	private String email;
	private String additionalContact;

	private String imageUrl;

	private String url;

	private ArrayList<String> partyAffiliations;

	private ArrayList<Office> offices;
	private District district;
	private Social social;

	public Senator()
	{
		this(0, "", "", "", "", "", "", "");
	}

	public Senator(Integer nid, String name, String lastName, String shortName, String email,
	        String additionalContact, String imageUrl, String url)
	{
		this(nid, name, lastName, shortName, email, additionalContact, imageUrl, url,
				new District(), new Social(), new ArrayList<Office>(), new ArrayList<String>());
	}

	public Senator(Integer nid, String name, String lastName, String shortName, String email,
	        String additionalContact, String imageUrl, String url, District district,
	        Social social, ArrayList<Office> offices, ArrayList<String> partyAffiliations)
	{
	    this.setNid(nid);
	    this.setName(name);
	    this.setLastName(lastName);
	    this.setShortName(shortName);
	    this.setEmail(email);
	    this.setAdditionalContact(additionalContact);
	    this.setImageUrl(imageUrl);
	    this.setUrl(url);
	    this.setPartyAffiliations(partyAffiliations);
	    this.setDistrict(district);
	    this.setOffices(offices);
	    this.setSocial(social);
	}

	public String getName()
	{
		return name;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getShortName()
	{
		return shortName;
	}

	public String getEmail()
	{
		return email;
	}

	public String getAdditionalContact()
	{
		return additionalContact;
	}

	public ArrayList<String> getPartyAffiliations()
	{
		return partyAffiliations;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public String getUrl()
	{
		return url;
	}

	public District getDistrict()
	{
		return district;
	}

	public ArrayList<Office> getOffices()
	{
	    return offices;
	}

	public Social getSocial()
	{
		return social;
	}

	public void setName(String name)
	{
		this.name = name==null ? "" : name;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName==null ? "" : lastName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName==null ? "" : shortName;
	}

	public void setEmail(String email)
	{
		this.email = email==null ? "" : email;
	}

	public void setAdditionalContact(String additionalContact)
	{
		this.additionalContact = additionalContact==null ? "" : additionalContact;
	}

	public void setPartyAffiliations(ArrayList<String> partyAffiliations)
	{
		this.partyAffiliations = partyAffiliations==null ? new ArrayList<String>() : partyAffiliations;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl==null ? "" : imageUrl;
	}

	public void setUrl(String url)
	{
		this.url = url==null ? "" : url;
	}

	public void setDistrict(District district)
	{
		this.district = district==null ? new District() : district;
	}

	public void setOffices(ArrayList<Office> offices)
	{
	    this.offices = offices==null ? new ArrayList<Office>() : offices;
	}

	public void setSocial(Social social)
	{
		this.social = social==null ? new Social() : social;
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
    public int compareTo(Senator o)
    {
        return this.getNid().compareTo(o.getNid());
    }
}
