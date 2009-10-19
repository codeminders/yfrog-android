/**
 * 
 */
package com.codeminders.yfrog.android.util;

import java.security.*;

/**
 * @author idemydenko
 * 
 */
public class MD5 {

	private MessageDigest md = null;
	static private MD5 md5 = null;
	private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private MD5() throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance("MD5");
	}

	public static MD5 getInstance() throws NoSuchAlgorithmException {
		if (md5 == null) {
			md5 = new MD5();
		}

		return (md5);
	}

	public String hashData(byte[] dataToHash) {
		return hexStringFromBytes((calculateHash(dataToHash)));
	}

	private byte[] calculateHash(byte[] dataToHash) {
		md.update(dataToHash, 0, dataToHash.length);
		return (md.digest());
	}

	public String hexStringFromBytes(byte[] bytes) {
		StringBuilder hex = new StringBuilder(32);

		int msb;
		int lsb;

		for (int i = 0; i < bytes.length; i++) {
			msb = ((bytes[i] & 0x000000FF) >>> 4);
			lsb = ((bytes[i] & 0x000000FF) % 16);
			hex.append(hexChars[msb]);
			hex.append(hexChars[lsb]);
		}
		return hex.toString();
	}
}
