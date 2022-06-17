package org.ituns.framework.master.tools.security;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class Encrypt {

    public static HashEncrypt md5() {
        return EncryptHolder.MD5;
    }

    public static HashEncrypt sha1() {
        return EncryptHolder.SHA1;
    }

    public static HashEncrypt sha256() {
        return EncryptHolder.SHA256;
    }

    public static HashEncrypt sha384() {
        return EncryptHolder.SHA384;
    }

    public static HashEncrypt sha512() {
        return EncryptHolder.SHA512;
    }

    private static class EncryptHolder {
        private static final HashEncrypt MD5 = new HashEncrypt(Hashing.md5());
        private static final HashEncrypt SHA1 = new HashEncrypt(Hashing.sha1());
        private static final HashEncrypt SHA256 = new HashEncrypt(Hashing.sha256());
        private static final HashEncrypt SHA384 = new HashEncrypt(Hashing.sha384());
        private static final HashEncrypt SHA512 = new HashEncrypt(Hashing.sha512());
    }

    public static class HashEncrypt {
        private final HashFunction function;

        private HashEncrypt(HashFunction function) {
            this.function = function;
        }

        public String encrypt(String text) {
            return encrypt(text, "UTF-8");
        }

        public String encrypt(String text, String charset) {
            return encrypt(text.getBytes(Charset.forName(charset)));
        }

        public String encrypt(byte[] bytes) {
            return function.hashBytes(bytes).toString();
        }
    }
}
