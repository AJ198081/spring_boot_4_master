package dev.aj.crypto_java.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class CryptoKeyManagerTest {

    private static final String KEY_PAIR_DIRECTORY = "crypto_command_line/key_pairs";
    private static final String CERTIFICATE_KEY_NAME = "certificate_key";
    private final CryptoKeyManager cryptoKeyManager = new CryptoKeyManager();

    @Test
    public void testGenerateKeyPair() throws NoSuchAlgorithmException {

        log.info("Ensuring RSA key pair...");

        cryptoKeyManager.persistRSAKeyPair(KEY_PAIR_DIRECTORY, CERTIFICATE_KEY_NAME);

        KeyPair keyPair = cryptoKeyManager.readKeyPair(KEY_PAIR_DIRECTORY, CERTIFICATE_KEY_NAME);

        assertNotNull(keyPair);

        CryptoKeyManager.DistinguishedName distinguishedName = new CryptoKeyManager.DistinguishedName(
                "Financial Industry",
                "www.dev.aj",
                "Engineering",
                "Sydney",
                "NSW",
                "AU"
        );

        X509Certificate x509Certificate = cryptoKeyManager.generateV3RootCertificate(
                keyPair,
                distinguishedName
        );

        log.info("Generated root certificate: {}", x509Certificate);

        log.info("Base64 encoded root certificate: {}", cryptoKeyManager.base64EncodeX509Certificate(x509Certificate));

        log.info("You can copy this base64 encoded certificate and try it in www.certlogik.com");
    }

}