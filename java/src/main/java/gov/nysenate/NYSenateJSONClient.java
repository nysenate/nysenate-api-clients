package gov.nysenate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

public class NYSenateJSONClient {
    private static final ObjectMapper jsonMapper = JsonMapper.builder()
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build();

    private static final String DEFAULT_DOMAIN = "https://www.nysenate.gov";
    private final String jsonUrl;

    public NYSenateJSONClient() {
        this(DEFAULT_DOMAIN);
    }

    public NYSenateJSONClient(String domain) {
        this.jsonUrl = (domain + "/senators.json").replaceAll("(?<!:)/+", "/");
    }

    /**
     * Returns a list of senators.
     * @return List<Senator>
     */
    public List<Senator> getSenators() {
        try {
            URL senatorsJsonUrl = new URL(jsonUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) senatorsJsonUrl.openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                String response = new String(httpURLConnection.getInputStream().readAllBytes());
                return Arrays.asList(jsonMapper.readValue(response, Senator[].class));
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
}
