package dev.aj.crypto_java;

import dev.aj.crypto_java.config.CryptoKeyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootApplication
@Slf4j
public class CryptoJavaApplication implements ApplicationListener<ApplicationStartedEvent> {


    private static final String CRYPTO_KEY_DIRECTORY = "crypto_command_line/key_pairs";
    private static final String KEY_NAME = "aj-rsa-test";

    static void main(String[] args) {
        SpringApplication.run(CryptoJavaApplication.class, args);

    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        CryptoKeyManager cryptoKeyManager = applicationContext.getBean(CryptoKeyManager.class);

        try {
            cryptoKeyManager.persistRSAKeyPair(CRYPTO_KEY_DIRECTORY, KEY_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        KeyPair keyPair = cryptoKeyManager.readKeyPair(CRYPTO_KEY_DIRECTORY, KEY_NAME);

        assertNotNull(keyPair);

        X509Certificate x509Certificate = cryptoKeyManager.generateV3RootCertificate(
                keyPair,
                new CryptoKeyManager.DistinguishedName(
                        "developer-world.com.au",
                        "aj-dev",
                        "engineering",
                        "Sydney",
                        "NSW",
                        "AU")
        );

        assertNotNull(x509Certificate);
        log.info("Generated root certificate -\n{}", x509Certificate);
    }
}
