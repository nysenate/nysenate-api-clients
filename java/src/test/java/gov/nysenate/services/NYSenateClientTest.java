package gov.nysenate.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import gov.nysenate.services.model.Committee;
import gov.nysenate.services.model.District;
import gov.nysenate.services.model.Member;
import gov.nysenate.services.model.Office;
import gov.nysenate.services.model.Senator;
import gov.nysenate.services.model.Social;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.BeforeClass;
import org.junit.Test;


public class NYSenateClientTest {

    private static NYSenateClient client;

    @BeforeClass
    public static void init() throws IOException {
        InputStream configStream = NYSenateClientTest.class.getResourceAsStream("/test.properties");
        if (configStream == null) {
            System.err.println("test.properties configuration file not found on classpath");
            System.exit(1);
        }

        Properties config = new Properties();
        config.load(configStream);

        String apiKey = config.getProperty("apiKey");
        String apiDomain = config.getProperty("apiDomain");
        if (apiKey == null || apiDomain == null) {
            throw new IllegalArgumentException("Make sure both apiKey and apiDomain properties have been set.");
        }

        client = new MemoryCachedNYSenateClient(apiDomain, apiKey);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor() {
        new MemoryCachedNYSenateClient("nysenate.gov", null);
    }

    @Test
    public void testGetSenators() throws XmlRpcException {
        ArrayList<Senator> senators = client.getSenators();
        assertEquals(62,senators.size()); // District 46 still undecided, total=63 soon
    }

    @Test
    public void testGetDistrict() throws XmlRpcException {
        District district = client.getDistrict(86);
        assertEquals(3, district.getNumber());
        assertEquals("http://geo.nysenate.gov/maps/regular.jsp?x=850&y=595&sd=03",district.getMapUrl());
        assertEquals("http://www.nysenate.gov/files/sd3.jpg", district.getImageUrl());
        assertEquals("http://www.nysenate.gov/district/03", district.getUrl());
    }

    @Test
    public void testGetCommittee() throws XmlRpcException {
        // Spot check with the Aging committee
        Committee committee = client.getCommittee(245);
        assertEquals("Aging", committee.getName());
        assertEquals("aging", committee.getShortName());
        assertEquals("http://www.nysenate.gov/committee/aging", committee.getUrl());

        // Check Chairs
        assertEquals(1,committee.getChairs().size());
        Member chair = committee.getChairs().get(0);
        assertEquals("David J. Valesky", chair.getName());
        assertEquals("valesky", chair.getShortName());
        assertEquals("http://www.nysenate.gov/senator/david-j-valesky", chair.getUrl());

        // Check member count order is arbitrary but consistent.
        // Greg Ball is the first one up, check on him.
        assertEquals(11, committee.getMembers().size());
        Member member = committee.getMembers().get(0);
        assertEquals("Greg Ball", member.getName());
        assertEquals("ball", member.getShortName());
        assertEquals("http://www.nysenate.gov/senator/greg-ball", member.getUrl());
    }

    @Test
    public void testGetSenator() throws XmlRpcException {
        Senator senator = client.getSenator(101);
        assertEquals("Last name error", "Dilan", senator.getLastName());
        assertEquals("Short name error", "dilan", senator.getShortName());
        assertEquals("Email error", "dilan@nysenate.gov", senator.getEmail());
        assertEquals("Full name error", "Martin Malavé Dilan" , senator.getName());
        assertEquals("Profile picture error", "http://www.nysenate.gov/files/profile-pictures/(05-16-12) Dilan-HS-005.jpg", senator.getImageUrl());
        assertEquals("Additional Contact Error", "", senator.getAdditionalContact());

        // Check his district
        District district = senator.getDistrict();
        assertEquals("District# Error", 18, district.getNumber());
        assertEquals("Map url error", "http://geo.nysenate.gov/maps/regular.jsp?x=850&y=595&sd=18",district.getMapUrl());
        assertEquals("Image url error", "http://www.nysenate.gov/files/sd18.jpg", district.getImageUrl());

        // Check his party affiliations
        assertArrayEquals("Party Affiliation Error", new Object[]{"D"}, senator.getPartyAffiliations().toArray());

        // Check his social links
        Social social = senator.getSocial();
        assertEquals("http://www.youtube.com/user/SenMMDilan", social.getYoutube());
        assertEquals("http://twitter.com/SenatorDilan", social.getTwitter());
        assertEquals("http://www.facebook.com/pages/Martin-Malave-Dilan/80483802077", social.getFacebook());
        assertEquals("", social.getFlickr());
        assertEquals("", social.getMyspace());
        assertEquals("", social.getPicasa());

        // Order is arbitrary but consistent, Albany is first
        ArrayList<Office> offices = senator.getOffices();
        assertEquals(2, offices.size());
        Office albanyOffice = offices.get(0);
        assertEquals("Albany Office", albanyOffice.getName());

        assertEquals("188 State Street", albanyOffice.getStreet());
        assertEquals("Room 903, Legislative Office Building", albanyOffice.getAdditional());
        assertEquals("Albany", albanyOffice.getCity());
        assertEquals("NY", albanyOffice.getProvince());
        assertEquals("New York", albanyOffice.getProvinceName());
        assertEquals("12247", albanyOffice.getPostalCode());
        assertEquals("us", albanyOffice.getCountry());
        assertEquals("United States", albanyOffice.getCountryName());

        assertEquals(42.652855, albanyOffice.getLatitude(), .00001);
        assertEquals(-73.759091, albanyOffice.getLongitude(), .00001);

        assertEquals("(518) 455-2177", albanyOffice.getPhone());
        assertEquals("(518) 426-6947", albanyOffice.getFax());
        assertEquals("", albanyOffice.getOtherPhone());


    }

    @Test
    public void testGetCommittees() throws XmlRpcException {
        ArrayList<Committee> committees = client.getStandingCommittees();
        assertEquals(33, committees.size()); // Pending creation of new committees
    }

}
