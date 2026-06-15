package gov.nysenate.services;

import gov.nysenate.services.model.Senator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NYSenateClientTest {
    private static NYSenateJSONClient client;

    @Before
    public void init() throws IOException {
        client = new NYSenateJSONClient();
    }

    @Test
    public void testClientReturnsData() {
        List<Senator> senators = client.getSenators();
        assertNotNull(client.getSenators());
        assertTrue(senators.size() >= 63);
    }
}
