package com.codeminders.yfrog.client.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author idemydenko
 *
 */
public class OAuthHelper {

	private static final String HMAC_SHA1 = "HmacSHA1";
	
	private static final Random RAND = new Random();
	
	private static final String TWITTER_VERIFY_URL = "http://twitter.com/account/verify_credentials.xml";
	
	private static final String PARAM_OAUTH_VERSION = "oauth_version";
	private static final String PARAM_OAUTH_NONCE = "oauth_nonce";
	private static final String PARAM_OAUTH_TIMESTAMP = "oauth_timestamp";
	private static final String PARAM_OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	private static final String PARAM_OAUTH_TOKEN = "oauth_token";
	private static final String PARAM_OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	private static final String PARAM_OAUTH_SIGNATURE = "oauth_signature";
	
	private static final String VAL_OAUTH_VERSION = "1.0";
	private static final String VAL_OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
	
	private static final String METHOD = "GET";
	
	public static String getOAuthVerifyUrl(String token, String tokenSecret, String consumerKey, String consumerSecret) {
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String nonce = String.valueOf(timestamp + RAND.nextInt());

        List<QueryParameter> oauthHeaderParams = new ArrayList<QueryParameter>(5);
        oauthHeaderParams.add(new QueryParameter(PARAM_OAUTH_CONSUMER_KEY, consumerKey));
        oauthHeaderParams.add(new QueryParameter(PARAM_OAUTH_SIGNATURE_METHOD, VAL_OAUTH_SIGNATURE_METHOD));
        oauthHeaderParams.add(new QueryParameter(PARAM_OAUTH_TIMESTAMP, timestamp));
        oauthHeaderParams.add(new QueryParameter(PARAM_OAUTH_NONCE, nonce));
        oauthHeaderParams.add(new QueryParameter(PARAM_OAUTH_VERSION, VAL_OAUTH_VERSION));
        oauthHeaderParams.add(new QueryParameter(PARAM_OAUTH_TOKEN, token));

        List<QueryParameter> signatureBaseParams = new ArrayList<QueryParameter>(oauthHeaderParams.size());
        signatureBaseParams.addAll(oauthHeaderParams);

        StringBuilder base = new StringBuilder(METHOD).append("&")
                .append(encode(TWITTER_VERIFY_URL)).append("&");
        
        base.append(encode(normalizeRequestParameters(signatureBaseParams)));

        String oauthBaseString = base.toString();
        String signature = generateSignature(oauthBaseString, consumerSecret, tokenSecret);

        oauthHeaderParams.add(new QueryParameter(PARAM_OAUTH_SIGNATURE, signature));
        return TWITTER_VERIFY_URL + "?" + encodeParameters(oauthHeaderParams);

	}

    private static String generateSignature(String data, String consumerSecret, String tokenSecret) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            SecretKeySpec spec;
            String oauthSignature = encode(consumerSecret) + "&" + encode(tokenSecret);
            spec = new SecretKeySpec(oauthSignature.getBytes(), HMAC_SHA1);
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ignore) {
            // should never happen
        }
        
        return new BASE64Encoder().encode(byteHMAC);
    }

    @SuppressWarnings("unchecked")
    private static String normalizeRequestParameters(List<QueryParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    private static String encodeParameters(List<QueryParameter> postParams) {
    	String splitter = "&";
        StringBuffer buf = new StringBuffer();

        for (QueryParameter param : postParams) {
            if (buf.length() != 0) {
                buf.append(splitter);
            }
            buf.append(encode(param.name)).append("=");
            buf.append(encode(param.value));
        }
        return buf.toString();
    }

    private static String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

	
}

class QueryParameter implements java.io.Serializable, Comparable<QueryParameter> {
	private static final long serialVersionUID = 7944519993214989813L;
	String name;
    String value;
    

    public QueryParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public QueryParameter(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public QueryParameter(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }

    @Override
    public int hashCode() {
    	final int prime = 31;
        int result = 13;
        result = prime * result + value.hashCode();
        return result;
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof QueryParameter)) {
        	return false;
        }
        
        QueryParameter castObj = (QueryParameter) obj;
        
        return this.name.equals(castObj.name) &&
            this.value.equals(castObj.value);
    }

    public int compareTo(QueryParameter o) {
        int compared;
        compared = name.compareTo(o.name);
        if (0 == compared) {
            compared = value.compareTo(o.value);
        }
        return compared;
    }
}


class BASE64Encoder {
    private static final char last2byte = (char) Integer.parseInt("00000011", 2);
    private static final char last4byte = (char) Integer.parseInt("00001111", 2);
    private static final char last6byte = (char) Integer.parseInt("00111111", 2);
    private static final char lead6byte = (char) Integer.parseInt("11111100", 2);
    private static final char lead4byte = (char) Integer.parseInt("11110000", 2);
    private static final char lead2byte = (char) Integer.parseInt("11000000", 2);
    private static final char[] encodeTable = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public BASE64Encoder() {
    }

    public String encode(byte[] from) {
        StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
        int num = 0;
        char currentByte = 0;
        for (int i = 0; i < from.length; i++) {
            num = num % 8;
            while (num < 8) {
                switch (num) {
                    case 0:
                        currentByte = (char) (from[i] & lead6byte);
                        currentByte = (char) (currentByte >>> 2);
                        break;
                    case 2:
                        currentByte = (char) (from[i] & last6byte);
                        break;
                    case 4:
                        currentByte = (char) (from[i] & last4byte);
                        currentByte = (char) (currentByte << 2);
                        if ((i + 1) < from.length) {
                            currentByte |= (from[i + 1] & lead2byte) >>> 6;
                        }
                        break;
                    case 6:
                        currentByte = (char) (from[i] & last2byte);
                        currentByte = (char) (currentByte << 4);
                        if ((i + 1) < from.length) {
                            currentByte |= (from[i + 1] & lead4byte) >>> 4;
                        }
                        break;
                }
                to.append(encodeTable[currentByte]);
                num += 6;
            }
        }
        if (to.length() % 4 != 0) {
            for (int i = 4 - to.length() % 4; i > 0; i--) {
                to.append("=");
            }
        }
        return to.toString();
    }
}
