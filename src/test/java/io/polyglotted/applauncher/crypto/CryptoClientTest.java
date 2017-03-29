package io.polyglotted.applauncher.crypto;

import org.testng.annotations.Test;

import static io.polyglotted.applauncher.crypto.CryptoClient.PASSWORD_SYSTEM_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CryptoClientTest {

    @Test
    public void shouldReturnRandomPassword() {
        CryptoClient cryptoClient = new CryptoClient();
        String encrypted = cryptoClient.encrypt("test");
        assertThat(encrypted, is("test"));
        String decrypted = cryptoClient.decrypt(encrypted);
        assertThat(decrypted, is("test"));
    }

    @Test
    public void shouldLookForPasswordFromSystemProperty() {
        System.setProperty(PASSWORD_SYSTEM_PROPERTY, "password");
        CryptoClient cryptoClient = new CryptoClient();
        String encrypted = cryptoClient.encrypt("test");
        assertThat(encrypted, is(not("test")));
        String decrypted = cryptoClient.decrypt(encrypted);
        assertThat(decrypted, is("test"));
        System.clearProperty(PASSWORD_SYSTEM_PROPERTY);
    }

    @Test
    public void shouldDecryptIfENCIndicatorPresent() {
        CryptoClient cryptoClient= new CryptoClient("password");
        String encrypted = cryptoClient.encrypt("test");
        String decrypted = cryptoClient.decrypt(encrypted);
        assertThat(decrypted, is("test"));
    }

    @Test
    public void shouldNotDecryptIfENCIndicatorNotPresent() {
        CryptoClient cryptoClient= new CryptoClient("password");
        String decrypted = cryptoClient.decrypt("test");
        assertThat(decrypted, is("test"));
    }

    @Test
    public void shouldNotAttemptToDecryptNullValue() {
        CryptoClient cryptoClient= new CryptoClient("password");
        String decrypted = cryptoClient.decrypt(null);
        assertThat(decrypted, is(nullValue()));
    }

    @Test
    public void shouldNotAttemptToDecryptEmptyValue() {
        CryptoClient cryptoClient= new CryptoClient("password");
        String decrypted = cryptoClient.decrypt("");
        assertThat(decrypted, is(""));
    }

    @Test
    public void shouldEncryptValueAndReturnENCFormatString() {
        CryptoClient cryptoClient= new CryptoClient("password");
        String encrypted = cryptoClient.encrypt("test");
        assertThat(encrypted, is(notNullValue()));
        assertThat(encrypted, startsWith("ENC("));
        assertThat(encrypted, endsWith(")"));
    }

    @Test
    public void shouldNotAttemptToEncryptNullValue() {
        CryptoClient cryptoClient= new CryptoClient("password");
        String encrypted = cryptoClient.encrypt(null);
        assertThat(encrypted, is(nullValue()));
    }

    @Test
    public void shouldNotAttemptToEncryptEmptyValue() {
        CryptoClient cryptoClient= new CryptoClient("password");
        String encrypted = cryptoClient.encrypt("");
        assertThat(encrypted, is(""));
    }
}