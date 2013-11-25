package gov.nysenate.services.model;

public class Social
{
	private String twitter;
	private String youtube;
	private String myspace;
	private String picasa;
	private String flickr;
	private String facebook;

	public Social()
	{
	    this("", "", "", "", "", "");
	}

	public Social(String twitter, String youtube, String myspace, String picasa,
	        String flickr, String facebook)
	{
		this.setTwitter(twitter);
		this.setYoutube(youtube);
		this.setMyspace(myspace);
		this.setPicasa(picasa);
		this.setFlickr(flickr);
		this.setFacebook(facebook);
	}

	public String getTwitter()
	{
		return twitter;
	}

	public String getYoutube()
	{
		return youtube;
	}

	public String getMyspace()
	{
		return myspace;
	}

	public String getPicasa()
	{
		return picasa;
	}

	public String getFlickr()
	{
		return flickr;
	}

	public String getFacebook()
	{
		return facebook;
	}

	public void setTwitter(String twitter)
	{
		this.twitter = twitter==null ? "" : twitter;
	}

	public void setYoutube(String youtube)
	{
		this.youtube = youtube==null ? "" : youtube;
	}

	public void setMyspace(String myspace)
	{
		this.myspace = myspace==null ? "" : myspace;
	}

	public void setPicasa(String picasa)
	{
		this.picasa = picasa==null ? "" : picasa;
	}

	public void setFlickr(String flickr)
	{
		this.flickr = flickr==null ? "" : flickr;
	}

	public void setFacebook(String facebook)
	{
		this.facebook = facebook==null ? "" : facebook;
	}
}