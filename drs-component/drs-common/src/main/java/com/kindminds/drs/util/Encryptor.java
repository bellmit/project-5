package com.kindminds.drs.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Encryptor {

    private static String key = "drs12345drs12345"; // 128 bit key
    private static String initVector = "drsdrsInitVector"; // 16 bytes IV

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            //Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

           String encryptedValue = Base64.encodeBase64String(encrypted);
           return URLEncoder.encode(encryptedValue,"UTF-8");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted , boolean containSpecialChar) {

        try {

        if(!containSpecialChar)
            encrypted =URLDecoder.decode(encrypted, "UTF-8");


            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /*
    static byte[] CIPHER_KEY = "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8);
    static byte[] IV = "1234567890ABCDEF".getBytes(StandardCharsets.UTF_8);
    static char PADDING_CHAR = '\034';

    public static String encrypt2(String text)  {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(CIPHER_KEY, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
            int paddingSize = 16 - text.length() % 16;
            String padding = String.format("%0" + paddingSize + "d", 0).replace('0', PADDING_CHAR);
            String padded = text + padding;
            byte[] encrypted = cipher.doFinal(padded.getBytes(StandardCharsets.UTF_8));
            return java.util.Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt2(String data)  {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(CIPHER_KEY, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
            byte[] encrypted = java.util.Base64.getDecoder().decode(data);
            String padded = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
            return padded.replaceAll(PADDING_CHAR + "+$", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    */





}