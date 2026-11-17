package gov.nysenate;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NYSenateClientTest {

    /** Set -Dnysenate.domain=https://... to run the live test against a staging site. */
    private static final String DOMAIN = System.getProperty("nysenate.domain");

    @Test
    public void testClientReturnsData() {
        var client = DOMAIN == null ? new NYSenateJSONClient() : new NYSenateJSONClient(DOMAIN);
        List<Senator> senators = client.getSenators();
        assertNotNull(senators);
        assertFalse(senators.isEmpty());
    }

    @Test
    public void testParsesFeed() throws IOException {
        List<Senator> senators = NYSenateJSONClient.parseSenators(readFixture());
        assertEquals(3, senators.size());

        Senator addabbo = senators.get(0);
        assertEquals("Joseph P. Addabbo Jr.", addabbo.getFullName());
        assertEquals("Joseph P.", addabbo.getFirstName());
        assertEquals("Addabbo Jr.", addabbo.getLastName());
        assertEquals("addabbo", addabbo.getShortName());
        assertEquals("addabbo@nysenate.gov", addabbo.getEmail());
        assertEquals(Integer.valueOf(384), addabbo.getOpenLegId());
        assertEquals(Integer.valueOf(15), addabbo.getDistrictNumber());
        assertEquals("https://www.nysenate.gov/district/15", addabbo.getDistrictUrl());
        assertEquals("https://www.nysenate.gov/senators/joseph-p-addabbo-jr", addabbo.getUrl());
        assertEquals("https://www.nysenate.gov/sites/default/files/headshot_addabbo-hs-profile.jpg",
                addabbo.getImageUrl());
        assertEquals(List.of("D"), addabbo.getParties());
        assertEquals("Chairman of Committee on Racing, Gaming and Wagering", addabbo.getRole());
        assertEquals("<p>Summary for Joseph P. Addabbo Jr..</p>", addabbo.getSummary());

        assertEquals(3, addabbo.getOffices().size());
        Office district = addabbo.getOffices().get(0);
        assertEquals("District Office", district.name());
        assertEquals("84-16 Jamaica Avenue", district.street());
        assertEquals("Woodhaven", district.city());
        assertEquals("11421", district.postalCode());
        assertEquals("(718) 738-1111", district.phone());
        assertEquals("(718) 296-0495", district.fax());
        assertEquals("", district.additional());
        assertEquals("NY", district.province());
        assertEquals("New York", district.provinceName());
        assertEquals("US", district.country());
        assertEquals("United States", district.countryName());

        assertEquals(Boolean.TRUE, addabbo.getActive());
        assertEquals("15th", addabbo.getDistrictOrdinal());
        assertEquals("https://www.nysenate.gov/sites/default/files/01-04-23_300-01-461.jpg",
                addabbo.getBannerImageUrl());
        assertEquals(new Palette("default", "#FFFFFF", "#FFFFFF", "#FFFFFF"), addabbo.getPalette());
        assertEquals(Map.of(
                "facebook", "http://www.facebook.com/senatorjoeaddabbo",
                "twitter", "https://twitter.com/SenJoeAddabbo",
                "youtube", "https://www.youtube.com/@SenatorJosephAddabbo/videos"),
                addabbo.getSocialMedia());
    }

    @Test
    public void testMissingSocialMediaIsEmpty() throws IOException {
        // Sutton has no social_media entry in the feed.
        Senator sutton = NYSenateJSONClient.parseSenators(readFixture()).get(1);
        assertEquals(Map.of(), sutton.getSocialMedia());
    }

    @Test
    public void testEmptyArrayForObjectFields() {
        // PHP encodes an empty map as [], so the client should tolerate it.
        String json = "{\"status\":\"ok\",\"messages\":[],\"data\":{\"count\":1,\"senators\":{\"x\":"
                + "{\"open_leg\":{\"member_id\":\"1\",\"shortname\":\"x\",\"is_active\":false},"
                + "\"profile\":{\"site\":{\"palette\":[]}},\"social_media\":[]}}}}";
        Senator senator = NYSenateJSONClient.parseSenators(json.getBytes()).get(0);
        assertEquals("x", senator.getShortName());
        assertEquals(Boolean.FALSE, senator.getActive());
        assertNull(senator.getPalette());
        assertEquals(Map.of(), senator.getSocialMedia());
    }

    @Test
    public void testShortNameComesFromOpenLegNotMapKey() throws IOException {
        // The feed keys this senator as "Sutton", but the shortname is "sutton".
        Senator sutton = NYSenateJSONClient.parseSenators(readFixture()).get(1);
        assertEquals("sutton", sutton.getShortName());
        assertEquals(Integer.valueOf(1572), sutton.getOpenLegId());
    }

    @Test
    public void testNullRole() throws IOException {
        Senator martins = NYSenateJSONClient.parseSenators(readFixture()).get(2);
        assertEquals("martins", martins.getShortName());
        assertNull(martins.getRole());
    }

    @Test(expected = RuntimeException.class)
    public void testMissingDataThrows() {
        NYSenateJSONClient.parseSenators(
                "{\"data\":null,\"messages\":[\"error\"],\"status\":\"error\"}".getBytes());
    }

    private static byte[] readFixture() throws IOException {
        try (InputStream in = NYSenateClientTest.class.getResourceAsStream("/senators-feed.json")) {
            assertNotNull("Missing test fixture", in);
            return in.readAllBytes();
        }
    }
}
