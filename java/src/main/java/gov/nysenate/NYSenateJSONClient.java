package gov.nysenate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

public class NYSenateJSONClient {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();
    private static final String DEFAULT_DOMAIN = "https://www.nysenate.gov";
    private static final ObjectMapper jsonMapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

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
            HttpRequest request = HttpRequest.newBuilder(URI.create(jsonUrl))
                .timeout(Duration.ofSeconds(10))
                //Set this to match nysenate.gov's allow-list
                .header("User-Agent", "Java/" + System.getProperty("java.version"))
                .GET().build();
            HttpResponse<byte[]> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == 200) {
                return List.of(jsonMapper.readValue(response.body(), Senator[].class));
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
}
