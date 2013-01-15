package gov.nysenate.services;

import gov.nysenate.services.NYSenateClient.METHOD;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class NYSenateService {
    private final Logger logger = Logger.getLogger(NYSenateClient.class);

    protected final XmlRpcClient client;
    protected final String apiDomain;
    protected final String apiKey;

    public NYSenateService(String apiDomain,  String apiKey) {
        if (apiDomain == null) {
            throw new IllegalArgumentException("apiDomain cannot be null");
        } else if (apiKey == null) {
            throw new IllegalArgumentException("apiKey cannot be null");
        } else {
            this.apiDomain = apiDomain;
            this.apiKey = apiKey;
        }

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL("http://www.nysenate.gov/services/xmlrpc"));
        } catch (MalformedURLException e) {
            // not possible since it is a hard-coded/tested url.
            System.out.println(e);
            throw new RuntimeException("Impossible Malformed URL error for: http://www.nysenate.gov/services/xmlrpc",e); // Just in case...
        }

        client = new XmlRpcClient();
        client.setConfig(config);
    }

    public Object getNode(Integer nid, String...fields) throws XmlRpcException {
        // Order of parameters matters!
        Object[] requiredParams = {nid};
        Object[] optionalParams = fields;
        return getXmlRpcResponse(METHOD.NODE_GET, concat(requiredParams,optionalParams));
    }

    public Object getView(String viewName, Object...viewParameters) throws XmlRpcException {
        return getView(viewName, null, null, null, false, viewParameters);
    }

    public Object getView(String viewName, String displayId, Object...viewParameters) throws XmlRpcException {
        return getView(viewName, displayId, null, null, false, viewParameters);
    }

    public Object getView(String viewName, Integer offset, Integer limit, Object...viewParameters) throws XmlRpcException {
        return getView(viewName, null, offset, limit, false, viewParameters);
    }

    public Object getView(String viewName, String displayId, Integer offset, Integer limit, Object...viewParameters) throws XmlRpcException {
        return getView(viewName, displayId, offset, limit, false, viewParameters);
    }

    public Object getView(String viewName, String displayId, Integer offset, Integer limit, boolean formatOutput, Object...viewParameters) throws XmlRpcException {
        // Order of parameters matters!
        Object[] requiredParams = {
            viewName,
            (displayId == null) ? "default" : displayId
        };
        Object[] optionalParams = viewParameters;
        Object[] viewParams = {
            (offset == null) ? 0 : offset,
            (limit == null) ? 0 : limit,
            formatOutput
        };

        return getXmlRpcResponse(METHOD.VIEWS_GET, concat(requiredParams, optionalParams, viewParams));
    }

    public Object getXmlRpcResponse(METHOD method, Object...parameters) throws XmlRpcException {
        // Computer security fields with current time stamp.
        long time = System.currentTimeMillis();
        String nonce = getMD5(String.valueOf(time)).substring(0, 20);
        String[] hashParts = {String.valueOf(time), apiDomain, nonce, method.getValue()};
        String hash = getHmacSHA256(join(";", hashParts));

        // Order of parameters counts; security first!
        List<Object> requestParameters = new ArrayList<Object>();
        requestParameters.addAll(Arrays.asList(hash, apiDomain, String.valueOf(time), nonce));
        requestParameters.addAll(Arrays.asList(parameters));
        logger.debug(method.getValue()+" - "+join(", ",parameters));
        return client.execute(method.getValue(), requestParameters);
    }


    // Here I implement a series of utility functions myself to avoid dependencies.
    private String getHmacSHA256(String source) {
        try {
            SecretKeySpec secret = new SecretKeySpec(apiKey.getBytes(),"HmacSHA256");

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secret);
            byte[] shaDigest = mac.doFinal(source.getBytes());

            return getHex(shaDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("hmacSHA256 should be available as a built in.");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("the secret key should be compatible with our MAC aglorithm.");
        }
    }

    private String getMD5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(source.getBytes());
            return getHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("md5 not available on this platform.");
        }
    }

    private String join(String separator, Object...parts) {
        StringBuffer sb = new StringBuffer(parts[0].toString());
        for (int i=1; i<parts.length; i++) {
            sb.append(separator+parts[i].toString());
        }
        return sb.toString();
    }

    private String getHex(byte[] source) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < source.length; ++i) {
            // toHexString does not 0 pad the hex string
            // 0x100).substring(1,3); does this padding
            sb.append(Integer.toHexString((source[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }

    private <T> T[] concat(T[]...args) {
        ArrayList<T> full = new ArrayList<T>();
        for (T[] arg : args) {
            full.addAll(Arrays.asList(arg));
        }
        return (T[])full.toArray();

    }
}
