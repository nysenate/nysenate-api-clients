package gov.nysenate.services;

import java.util.List;

import gov.nysenate.services.model.Senator;


public interface NYSenateClientService
{
    /**
     * Returns a list of senators.
     * @return List<Senator>
     */
    List<Senator> getSenators();
}
