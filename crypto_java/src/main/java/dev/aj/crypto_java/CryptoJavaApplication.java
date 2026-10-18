package dev.aj.crypto_java;

import dev.aj.crypto_java.config.CryptoKeyManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootApplication
public class CryptoJavaApplication implements ApplicationListener<ContextRefreshedEvent> {


    static void main(String[] args) {
        SpringApplication.run(CryptoJavaApplication.class, args);

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        CryptoKeyManager cryptoKeyManager = applicationContext.getBean(CryptoKeyManager.class);

        try {
            cryptoKeyManager.persistRSAKeyPair("crypto_command_line/key_pairs", "aj-rsa-test");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        KeyPair keyPair = cryptoKeyManager.readKeyPair("crypto_command_line/key_pairs", "aj-rsa-test-none");

        assertNotNull(keyPair);
    }
}
