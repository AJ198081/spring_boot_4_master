package dev.aj.crypto_java;

import dev.aj.crypto_java.config.CryptoKeyManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.security.KeyPair;

@SpringBootApplication
public class CryptoJavaApplication implements ApplicationListener<ContextRefreshedEvent> {


    static void main(String[] args) {
        SpringApplication.run(CryptoJavaApplication.class, args);

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        CryptoKeyManager cryptoKeyManager = applicationContext.getBean(CryptoKeyManager.class);

        KeyPair publicKeyCertification = cryptoKeyManager.getPublicKeyCertification();

        assert publicKeyCertification != null;
    }
}
