package com.kindminds.drs.service.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Encrypter {
	public static String enrypt(final String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		StringBuffer sb = new StringBuffer();
        try {
            final java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(str.getBytes("UTF-8"));
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (final java.security.NoSuchAlgorithmException e) {
        	throw e;
        }
    }
}
