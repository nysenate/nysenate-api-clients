package gov.nysenate.services;

import gov.nysenate.services.model.Committee;
import gov.nysenate.services.model.District;
import gov.nysenate.services.model.Senator;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

public class MemoryCachedNYSenateClient extends NYSenateClient {
    private final Logger logger = Logger.getLogger(NYSenateClient.class);

    private final HashMap<Integer, Object> cache;

    public MemoryCachedNYSenateClient(String apiDomain,  String apiKey) {
        super(apiDomain,  apiKey);
        cache = new HashMap<Integer, Object>();
    }

    public Senator getSenator(int nid) throws XmlRpcException {
        if (!cache.containsKey(nid)) {
            logger.debug("Cache Miss "+nid+" - senator");
            cache.put(nid, super.getSenator(nid));
        } else {
            logger.debug("Cache Hit "+nid+" - senator");
        }
        return (Senator)cache.get(nid);
    }

    public District getDistrict(int nid) throws XmlRpcException {
        if (!cache.containsKey(nid)) {
            logger.debug("Cache Miss "+nid+" - district");
            cache.put(nid, super.getDistrict(nid));
        } else {
            logger.debug("Cache Hit "+nid+" - district");
        }
        return (District)cache.get(nid);
    }

    public Committee getCommittee(int nid) throws XmlRpcException {
        if (!cache.containsKey(nid)) {
            logger.debug("Cache Miss "+nid+" - committee");
            cache.put(nid, super.getCommittee(nid));
        } else {
            logger.debug("Cache Hit "+nid+" - committee");
        }
        return (Committee)cache.get(nid);
    }

}
