package gov.nysenate.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nysenate.services.model.District;
import gov.nysenate.services.model.Office;
import gov.nysenate.services.model.Senator;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class NYSenateJSONClient implements NYSenateClientService
{
    private final Logger logger = Logger.getLogger(NYSenateJSONClient.class);

    protected static final ObjectMapper jsonMapper = new ObjectMapper();

    protected static Properties prop = null;
    protected String baseUrl = "http://www.nysenate.gov";

    public NYSenateJSONClient() throws IOException {
        init(null);
    }

    public NYSenateJSONClient(String domain) throws IOException {
        init(domain);
    }

    private void init(String domain) throws IOException {
        if (domain == null) {
            InputStream in = getClass().getClassLoader().getResourceAsStream("app.properties");
            if (in != null) {
                prop = new Properties();
                prop.load(in);
                String propDomain = prop.getProperty("nysenate.domain");
                if (propDomain != null) {
                    baseUrl = propDomain;
                }
            }
        }
        else {
            baseUrl = domain;
        }
        // Strip trailing slash
        baseUrl = baseUrl.replaceFirst("/$", "");
    }

    @Override
    public List<Senator> getSenators() {
        HttpURLConnection httpURLConnection = null;
        try {
            URL senatorsJsonUrl = new URL(baseUrl + "/senators-json");
            httpURLConnection = (HttpURLConnection) senatorsJsonUrl.openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                String response = IOUtils.toString(httpURLConnection.getInputStream());
                return getSenatorsFromResponse(response);
            }
            else {
                throw new RuntimeException(
                    "Failed to fetch a valid response from the NYSenate.gov API! " +
                    "HTTP Status Code: " + httpURLConnection.getResponseCode());
            }
        }
        catch (MalformedURLException ex) {
            throw new RuntimeException("Failed to make request!", ex);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to fetch response!", ex);
        }
    }

    private static List<Senator> getSenatorsFromResponse(String response) throws IOException {
        JsonNode jsonNode = jsonMapper.readTree(response);
        List<Senator> senators = new ArrayList<>();
        jsonNode.elements().forEachRemaining((senatorNode) -> senators.add(getSenatorFromNode(senatorNode)));
        return senators;
    }

    private static Senator getSenatorFromNode(JsonNode node) {
        Senator senator = new Senator();
        senator.setDistrict(new District(node.get("senate_district").asInt(), node.get("senate_district_url").asText()));
        senator.setShortName(node.get("short_name").asText());
        senator.setEmail(node.get("email").asText());
        senator.setName(node.get("full_name").asText());
        senator.setFirstName(node.get("first_name").asText());
        senator.setLastName(node.get("last_name").asText());
        senator.setOpenLegId(node.get("open_leg_id").asInt());
        senator.setUrl(node.get("url").asText());
        List<String> partyList = new ArrayList<>();
        for (final JsonNode partyNode : node.get("party")) {
            partyList.add(partyNode.asText());
        }
        senator.setPartyAffiliations(partyList);
        senator.setImageUrl(node.get("img").asText());
        senator.setSummary(node.get("summary").asText());
        List<Office> offices = new ArrayList<>();
        for (final JsonNode officeNode : node.get("offices")) {
            Office office = new Office();
            office.setName(officeNode.get("name").asText());
            office.setStreet(officeNode.get("street").asText());
            office.setCity(officeNode.get("city").asText());
            office.setPostalCode(officeNode.get("postal_code").asText());
            office.setProvince(officeNode.get("province").asText());
            office.setCountry(officeNode.get("country_name").asText());
            office.setPhone(officeNode.get("phone").asText());
            office.setFax(officeNode.get("fax").asText());
            office.setAdditional(officeNode.get("additional").asText());
            office.setLatitude(officeNode.get("latitude").asDouble());
            office.setLongitude(officeNode.get("longitude").asDouble());
            offices.add(office);
        }
        senator.setOffices(offices);
        return senator;
    }
}
