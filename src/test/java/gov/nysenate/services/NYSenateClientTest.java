package gov.nysenate.services;

import gov.nysenate.services.model.Senator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NYSenateClientTest
{
    private static NYSenateJSONClient client;

    private static final Logger logger = Logger.getLogger(NYSenateClientTest.class);

    @Before
    public void init() throws IOException {
        client = new NYSenateJSONClient();
    }

    @Test
    public void testClientReturnsData() throws Exception {
        List<Senator> senators = client.getSenators();
        assertNotNull(client.getSenators());
        assertTrue(senators.size() >= 63);
    }
}
