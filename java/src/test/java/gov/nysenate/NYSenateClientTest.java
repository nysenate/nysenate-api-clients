package gov.nysenate;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NYSenateClientTest {
    private static final NYSenateJSONClient client = new NYSenateJSONClient();

    @Test
    public void testClientReturnsData() {
        List<Senator> senators = client.getSenators();
        assertNotNull(senators);
        assertFalse(senators.isEmpty());
    }
}
