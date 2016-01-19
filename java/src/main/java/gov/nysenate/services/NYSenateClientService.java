package gov.nysenate.services;

import gov.nysenate.services.model.Senator;

import java.util.List;

public interface NYSenateClientService
{
    /**
     * Returns a list of senators.
     * @return List<Senator>
     */
    List<Senator> getSenators();
}
