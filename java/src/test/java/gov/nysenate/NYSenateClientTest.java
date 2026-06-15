package gov.nysenate;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(63, senators.size());
    }
}
