package gov.nysenate.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.commons.io.IOUtils;

import gov.nysenate.services.model.Senator;

public class NYSenateJSONClient implements NYSenateClientService {
    protected static final ObjectMapper jsonMapper = new ObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected static Properties prop = null;
    protected String baseUrl = "https://www.nysenate.gov";

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
        try {
            URL senatorsJsonUrl = new URL(baseUrl + "/senators.json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) senatorsJsonUrl.openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                String response = IOUtils.toString(httpURLConnection.getInputStream(), Charset.defaultCharset());
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
