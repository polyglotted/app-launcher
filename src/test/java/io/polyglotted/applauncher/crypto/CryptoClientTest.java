package io.polyglotted.applauncher.crypto;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.polyglotted.applauncher.crypto.CryptoClient.PASSWORD_SYSTEM_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CryptoClientTest {

    private CryptoClient cryptoClient;

    @BeforeTest
    public void setUp() { cryptoClient = new CryptoClient("password"); }

    @Test
    public void shouldReturnRandomPassword() { cryptoClient = new CryptoClient(); }

    @Test
    public void shouldLookForPasswordFromSystemProperty() {
        System.setProperty(PASSWORD_SYSTEM_PROPERTY, "password");
        cryptoClient = new CryptoClient();
        System.clearProperty(PASSWORD_SYSTEM_PROPERTY);
    }

    @Test
    public void shouldDecryptIfENCIndicatorPresent() {
        String encrypted = cryptoClient.encrypt("test");
        String decrypted = cryptoClient.decrypt(encrypted);
        assertThat(decrypted, is("test"));
    }

    @Test
    public void shouldNotDecryptIfENCIndicatorNotPresent() {
        String decrypted = cryptoClient.decrypt("test");
        assertThat(decrypted, is("test"));
    }

    @Test
    public void shouldNotAttemptToDecryptNullValue() {
        String decrypted = cryptoClient.decrypt(null);
        assertThat(decrypted, is(nullValue()));
    }

    @Test
    public void shouldNotAttemptToDecryptEmptyValue() {
        String decrypted = cryptoClient.decrypt("");
        assertThat(decrypted, is(""));
    }

    @Test
    public void shouldEncryptValueAndReturnENCFormatString() {
        String encrypted = cryptoClient.encrypt("test");
        assertThat(encrypted, is(notNullValue()));
        assertThat(encrypted, startsWith("ENC("));
        assertThat(encrypted, endsWith(")"));
    }

    @Test
    public void shouldNotAttemptToEncryptNullValue() {
        String encrypted = cryptoClient.encrypt(null);
        assertThat(encrypted, is(nullValue()));
    }

    @Test
    public void shouldNotAttemptToEncryptEmptyValue() {
        String encrypted = cryptoClient.encrypt("");
        assertThat(encrypted, is(""));
    }
}