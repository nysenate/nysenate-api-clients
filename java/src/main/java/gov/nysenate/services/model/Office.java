package gov.nysenate.services.model;

public class Office {
	private String name;

	private String street;
	private String city;
	private String postalCode;
	private String provinceName;
	private String province;
	private String countryName;
	private String country;

	private String phone;
	private String fax;
	private String otherPhone;
	private String additional;

	private double latitude;
	private double longitude;

	public Office() {
	    this("", "", "", "", "" ,"", "" ,"", "", "", "", "", 0, 0);
	}

	public Office(String name, String street, String city,
			String postalCode, String provinceName, String province,
			String countryName, String country, String phone, String fax,
			String otherPhone, String additional, double latitude,
			double longitude) {

		this.setName(name);
		this.setStreet(street);
		this.setCity(city);
		this.setPostalCode(postalCode);
		this.setProvinceName(provinceName);
		this.setProvince(province);
		this.setCountryName(countryName);
		this.setCountry(country);
		this.setPhone(phone);
		this.setOtherPhone(otherPhone);
		this.setFax(fax);
		this.setAdditional(additional);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}
	public String getName() {
		return name;
	}
	public String getStreet() {
		return street;
	}
	public String getCity() {
		return city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public String getProvince() {
		return province;
	}
	public String getCountryName() {
		return countryName;
	}
	public String getCountry() {
		return country;
	}
	public String getPhone() {
		return phone;
	}
	public String getFax() {
		return fax;
	}
	public String getOtherPhone() {
		return otherPhone;
	}
	public String getAdditional() {
		return additional;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setName(String name) {
		this.name = name==null ? "" : name;
	}
	public void setStreet(String street) {
		this.street = street==null ? "" : street;
	}
	public void setCity(String city) {
		this.city = city==null ? "" : city;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode==null ? "" : postalCode;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName==null ? "" : provinceName;
	}
	public void setProvince(String province) {
		this.province = province==null ? "" : province;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName==null ? "" : countryName;
	}
	public void setCountry(String country) {
		this.country = country==null ? "" : country;
	}
	public void setPhone(String phone) {
		this.phone = phone==null ? "" : phone;
	}
	public void setFax(String fax) {
		this.fax = fax==null ? "" : fax;
	}
	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone==null ? "" : otherPhone;
	}
	public void setAdditional(String additional) {
		this.additional = additional==null ? "" : additional;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
