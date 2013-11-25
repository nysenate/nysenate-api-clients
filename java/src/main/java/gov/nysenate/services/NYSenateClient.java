package gov.nysenate.services;

import gov.nysenate.services.model.Committee;
import gov.nysenate.services.model.District;
import gov.nysenate.services.model.Member;
import gov.nysenate.services.model.Office;
import gov.nysenate.services.model.Senator;
import gov.nysenate.services.model.Social;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

public class NYSenateClient extends NYSenateService {
    public static enum METHOD {
        NODE_GET("node.get"),VIEWS_GET("views.get");
        private final String value;
        private METHOD(String value) { this.value = value; }
        public String getValue() { return this.value; }
    }

    private final Logger logger = Logger.getLogger(NYSenateClient.class);
    protected final String BASE_URL = "http://www.nysenate.gov/";

	public NYSenateClient(String apiDomain,  String apiKey) {
	    super(apiDomain, apiKey);
	}

    @SuppressWarnings("unchecked")
    public ArrayList<Committee> getStandingCommittees() throws XmlRpcException {
        ArrayList<Committee> committees = new ArrayList<Committee>();
        for(Object committee:as(Object[].class, getView("committees", "block_1"))) {
            HashMap<String,Object> committeeMap = as(HashMap.class, committee);
            Integer nid = new Integer(as(String.class,committeeMap.get("nid")));
            committees.add(getCommittee(nid));
        }
        return committees;
    }

    @SuppressWarnings("unchecked")
    public Committee getCommittee(int nid) throws XmlRpcException {
        logger.debug("Fetching committee node id: "+nid);
        Object node = getNode(nid);
        HashMap<String,Object> committeeMap = as(HashMap.class, node);

        if(!as(String.class,committeeMap.get("type")).equals("committee"))
            return null;

        String name = as(String.class, committeeMap.get("title")).trim();
        logger.info("Got node id: " + nid + " for committee " + name);
        String shortName = unwrap(String.class, committeeMap.get("field_short_name"), "value").trim();
        String videoUrl = "http://www.livestream.com/"+unwrap(String.class, committeeMap.get("field_video"), "value").trim();
        String url = BASE_URL + as(String.class, committeeMap.get("path"));
        ArrayList<Member> members = getMembers(as(Object[].class, committeeMap.get("field_multi_senator")));
        ArrayList<Member> chairs = getMembers(as(Object[].class, committeeMap.get("field_chairs")));
        return new Committee(nid, name, shortName, url, videoUrl, chairs, members);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Senator> getSenators() throws XmlRpcException {
        ArrayList<Senator> senators = new ArrayList<Senator>();
        for(Object senator: as(Object[].class,getView("senators"))) {
            HashMap<String,Object> senatorMap = as(HashMap.class, senator);
            Integer nid = new Integer(as(String.class,senatorMap.get("nid")));
            senators.add(getSenator(nid));
        }
        return senators;
    }

    @SuppressWarnings("unchecked")
    public Senator getSenator(int nodeId) throws XmlRpcException {
        logger.debug("Fetching senator node id: "+nodeId);
        HashMap<String,Object> senatorMap = as(HashMap.class, getNode(nodeId));

        if(!as(String.class,senatorMap.get("type")).equals("senator"))
            return null;

        String name = as(String.class, senatorMap.get("title")).trim();
        logger.info("Got node id: " + nodeId + " for senator " + name);
        String lastName = unwrap(String.class, senatorMap.get("field_last_name"), "value").split(",")[0].trim();
        String shortName = unwrap(String.class, senatorMap.get("field_short_name"), "value").trim();
        String email = unwrap(String.class, senatorMap.get("field_email"), "email").trim();
        String additionalContact = unwrap(String.class, senatorMap.get("field_extra_contact_information"), "value").trim();

        String url = BASE_URL + as(String.class, senatorMap.get("path"));
        String imageUrl = BASE_URL + unwrap(String.class, senatorMap.get("field_profile_picture"), "filepath");

        ArrayList<String> partyAffiliations = new ArrayList<String>();
        for(Object object: as(Object[].class, senatorMap.get("field_party_affiliation"))) {
            HashMap<String,Object> partyAffilationMap = as(HashMap.class, object);
            String value = as(String.class, partyAffilationMap.get("value")).trim();
            if (!value.isEmpty()) {
                partyAffiliations.add(value);
            }
        }

        String districtNodeId = unwrap(String.class, senatorMap.get("field_senators_district"), "nid");
        District district = getDistrict(Integer.parseInt(districtNodeId));

        String twitter = unwrap(String.class, senatorMap.get("field_twitter_link"), "url");
        String youtube = unwrap(String.class, senatorMap.get("field_youtube_link"), "url");
        String myspace = unwrap(String.class, senatorMap.get("field_myspace_link"), "url");
        String picasa = unwrap(String.class, senatorMap.get("field_picasa_link"), "url");
        String flickr = unwrap(String.class, senatorMap.get("field_flickr_link"), "url");
        String facebook = unwrap(String.class, senatorMap.get("field_facebook_link"), "url");
        Social senatorSocial = new Social(twitter, youtube, myspace, picasa, flickr, facebook);

        ArrayList<Office> offices = new ArrayList<Office>();
        for(Object object:as(Object[].class, senatorMap.get("locations"))) {
            offices.add(getOffice(as(HashMap.class, object)));
        }

        Senator senator = new Senator(nodeId, name, lastName, shortName, email, additionalContact,
                imageUrl, url, district, senatorSocial, offices, partyAffiliations);

        return senator;
    }

    @SuppressWarnings("unchecked")
    public District getDistrict(int nid) throws XmlRpcException {
        logger.debug("Fetching district node id: "+nid);
        HashMap<String,Object> districtMap = as(HashMap.class, getNode(nid));
        int districtNumber = new Integer(unwrap(String.class, districtMap.get("field_district_number"), "value"));
        String districtImageUrl = BASE_URL + unwrap(String.class, districtMap.get("field_district_map"),"filepath");
        String districtSageUrl = unwrap(String.class, districtMap.get("field_sage_map"),"url");
        String districtUrl = BASE_URL + as(String.class, districtMap.get("path"));
        return new District(districtNumber, districtUrl,  districtImageUrl, districtSageUrl);
    }

    private Office getOffice(HashMap<String, String> officeMap) {
        String officeName = as(String.class, officeMap.get("name"));

        String street = as(String.class, officeMap.get("street")).trim();
        String city = as(String.class, officeMap.get("city")).trim();
        String postalCode = as(String.class, officeMap.get("postal_code")).trim();
        String provinceName = as(String.class, officeMap.get("province_name")).trim();
        String province = as(String.class, officeMap.get("province")).trim();
        String countryName = as(String.class, officeMap.get("country_name")).trim();
        String country = as(String.class, officeMap.get("country")).trim();

        String phone = as(String.class, officeMap.get("phone")).trim();
        String fax = as(String.class, officeMap.get("fax")).trim();
        String otherPhone = as(String.class, officeMap.get("otherphone")).trim();
        String additional = as(String.class, officeMap.get("additional")).trim();

        double latitude = new Double(as(String.class, officeMap.get("latitude")));
        double longitude = new Double(as(String.class, officeMap.get("longitude")));

        return new Office(officeName, street, city, postalCode,
                provinceName, province, countryName, country, phone,
                fax, otherPhone, additional, latitude, longitude);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Member> getMembers(Object[] memberObjects) throws XmlRpcException {
        ArrayList<Member> members = new ArrayList<Member>();
        for(Object object:memberObjects) {
            HashMap<String,Object> memberMap = as(HashMap.class, object);
            String nid_string = as(String.class, memberMap.get("nid"));
            try {
                int nid = new Integer(nid_string);
                members.add(new Member(getSenator(nid)));
            } catch (XmlRpcException e) {
                logger.error("Member could not be retrieved.",e);
            } catch (NumberFormatException e) {
                logger.warn("invalid nid `"+nid_string+"` found. Skipping");
            }
        }

        // TODO: Are there actually duplicates in the data?
//        TreeSet<Member> set = new TreeSet<Member>(new Comparator<Member>() {
//              public int compare(Member one, Member two) {
//                  return one.getShortName().compareTo(two.getShortName());
//              }
//          });
//        set.addAll(members);
//        members.clear();
//        members.addAll(set);

        return members;
    }

    //////////////////////////////////////////////////////
    /// Response Parse Helpers
    //////////////////////////////////////////////////////



    @SuppressWarnings("unchecked")
    private <T> T unwrap(Class<T> clazz, Object object, String key) {
        return as(clazz, ((HashMap<String,Object>)((Object[])object)[0]).get(key));
    }

    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> clazz, Object object) {
        if(object == null || clazz == null)
            return null;
        else if (clazz.isInstance(object))
            return (T) object;
        else {
            //raise some sort of exception here..
        }
        return null;
    }
}
