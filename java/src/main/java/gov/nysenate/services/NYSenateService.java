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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

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
        client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
        client.setConfig(config);
    }

    public Object getNode(Integer nid, String...fields) throws XmlRpcException {
        // Order of parameters matters!
        Object[] requiredParams = {nid};
        Object[] optionalParams = fields;
        return getXmlRpcResponse(METHOD.NODE_GET, ArrayUtils.addAll(requiredParams,optionalParams));
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

        return getXmlRpcResponse(METHOD.VIEWS_GET, ArrayUtils.addAll(ArrayUtils.addAll(requiredParams, optionalParams), viewParams));
    }

    public Object getXmlRpcResponse(METHOD method, Object...parameters) throws XmlRpcException {
        // Computer security fields with current time stamp.
        long time = System.currentTimeMillis();
        String nonce = generateServiceNonce(time);
        String hash = generateServicesHash(time, nonce, method.getValue());

        // Order of parameters counts; security first!
        List<Object> requestParameters = new ArrayList<Object>();
        requestParameters.addAll(Arrays.asList(hash, apiDomain, String.valueOf(time), nonce));
        requestParameters.addAll(Arrays.asList(parameters));
        logger.debug(method.getValue()+" - "+StringUtils.join(parameters,", "));
        return client.execute(method.getValue(), requestParameters);
    }

    public String generateServiceNonce(long time) {
        MessageDigest md5 = DigestUtils.getMd5Digest();
        byte[] md5Digest = md5.digest(String.valueOf(time).getBytes());
        return Hex.encodeHexString(md5Digest).substring(0,20);
    }

    public String generateServicesHash(long time, String nonce, String methodName) {
        try {
            String[] hashParts = {String.valueOf(time), apiDomain, nonce, methodName};
            SecretKeySpec secret = new SecretKeySpec(apiKey.getBytes(),"HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secret);
            byte[] shaDigest = mac.doFinal(StringUtils.join(hashParts, ";").getBytes());
            return Hex.encodeHexString(shaDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("hmacSHA256 should be available as a built in.");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("the secret key should be compatible with our MAC aglorithm.");
        }
    }
}
