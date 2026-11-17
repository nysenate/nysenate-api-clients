package gov.nysenate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

public class NYSenateJSONClient {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private static final String DEFAULT_DOMAIN = "https://www.nysenate.gov";
    private static final String SENATORS_PATH = "/nys_feeds/senators";
    private static final ObjectMapper jsonMapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // PHP serializes an empty map as [], so accept that for object fields
            .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
            .build();

    private final String jsonUrl;

    public NYSenateJSONClient() {
        this(DEFAULT_DOMAIN);
    }

    public NYSenateJSONClient(String domain) {
        this.jsonUrl = (domain + SENATORS_PATH).replaceAll("(?<!:)/+", "/");
    }

    /**
     * Returns a list of senators.
     * @return List<Senator>
     */
    public List<Senator> getSenators() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(jsonUrl))
                .timeout(Duration.ofSeconds(10))
                //Set this to match nysenate.gov's allow-list
                .header("User-Agent", "Java/" + System.getProperty("java.version"))
                .GET().build();
            HttpResponse<byte[]> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == 200) {
                return parseSenators(response.body());
            }
            throw new RuntimeException(
                "Failed to fetch a valid response from the NYSenate.gov API! " +
                "HTTP Status Code: " + response.statusCode());
        }
        catch (HttpTimeoutException ex) {
            throw new RuntimeException("Request/response timed out!", ex);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to fetch response!", ex);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch response!", ex);
        }
    }

    /**
     * Parses the body of the senators feed into a list of senators.
     * @param json the raw feed response
     * @return List<Senator>, in feed order
     */
    static List<Senator> parseSenators(byte[] json) {
        SenatorFeed feed = jsonMapper.readValue(json, SenatorFeed.class);
        if (feed.data() == null || feed.data().senators() == null) {
            throw new RuntimeException(
                "The NYSenate.gov API returned no senator data! " +
                "Status: " + feed.status() + ", messages: " + feed.messages());
        }
        return feed.data().senators().values().stream()
            .filter(Objects::nonNull)
            .map(SenatorFeed.Entry::toSenator)
            .toList();
    }
}
