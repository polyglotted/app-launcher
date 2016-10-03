package io.polyglotted.applauncher.crypto;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import static java.util.UUID.randomUUID;

public final class CryptoClient {
    public static final String PASSWORD_SYSTEM_PROPERTY = "crypto.password";
    private static final String PREFIX = "ENC(";
    private static final String SUFFIX = ")";
    private final StringEncryptor encryptor;

    public CryptoClient() { this(resolvePassword()); }

    public CryptoClient(String password) { this.encryptor = createEncryptor(password); }

    public String encrypt(String toEncrypt) {
        return (isNullOrEmpty(toEncrypt)) ? toEncrypt : String.format("%s%s%s", PREFIX, encryptor.encrypt(toEncrypt), SUFFIX);
    }

    public String decrypt(String encrypted) {
        if (!isNullOrEmpty(encrypted) && encrypted.startsWith(PREFIX) && encrypted.endsWith(SUFFIX)) {
            encrypted = encrypted.substring(PREFIX.length(), encrypted.length() - SUFFIX.length());
            return encryptor.decrypt(encrypted);
        }
        return encrypted;
    }

    private static String resolvePassword() {
        String password = System.getProperty(PASSWORD_SYSTEM_PROPERTY);
        if (isNullOrEmpty(password)) {
            password = System.getenv(PASSWORD_SYSTEM_PROPERTY);
        }
        return isNullOrEmpty(password) ? randomUUID().toString() : password;
    }

    private static StringEncryptor createEncryptor(String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        return encryptor;
    }

    private static boolean isNullOrEmpty(String word) { return word == null || "".equals(word); }
}
