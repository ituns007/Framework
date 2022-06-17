package org.ituns.framework.master.tools.security;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes {
    private static final String CHARSET = "UTF-8";
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

    public static String encrypt(String content, String key) throws Exception {
        return encrypt(content, key, CHARSET);
    }

    public static String encrypt(String content, String key, String charset) throws Exception {
        byte[] c = content.getBytes(charset);
        byte[] k = key.getBytes(charset);
        byte[] result = encrypt(c, k);
        byte[] encode = Base64.encode(result, Base64.DEFAULT);
        return new String(encode, charset);
    }

    private static byte[] encrypt(byte[] content, byte[] key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivps = new IvParameterSpec(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
        return cipher.doFinal(content);
    }

    public static String decrypt(String content, String key) throws Exception {
        return decrypt(content, key, CHARSET);
    }

    public static String decrypt(String content, String key, String charset) throws Exception {
        byte[] c = content.getBytes(charset);
        byte[] decode = Base64.decode(c, Base64.DEFAULT);
        byte[] k = key.getBytes(charset);
        byte[] result = decrypt(decode, k);
        return new String(result, charset);
    }

    private static byte[] decrypt(byte[] content, byte[] key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivps = new IvParameterSpec(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
        return cipher.doFinal(content);
    }
}
