package dev.aj.crypto_java.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

@Component
@Slf4j
public class CryptoConfiguration {


    public static final String USER_DIR = "user.dir";
    public static final String CRYPTO_COMMAND_LINE_DIR = "crypto_command_line";
    public static final String RSA_PRIVATE_KEY_FILE = "aj-rsa.pem";

    @PostConstruct
    public KeyPair configure() throws NoSuchAlgorithmException {

        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

//     KeySize is the number of bits in the generated public/private key
        keyPairGenerator.initialize(2048);

        KeyPair asymmetricKeyPair = keyPairGenerator.generateKeyPair();

        log.info("Asymmetric key pair generated successfully");
        log.debug("Public key: {}", asymmetricKeyPair.getPublic());
        log.debug("Private key: {}", asymmetricKeyPair.getPrivate());

        Path privateKeyFilePath = Path.of(System.getProperty(USER_DIR), CRYPTO_COMMAND_LINE_DIR).resolve(RSA_PRIVATE_KEY_FILE);

        if (!Files.exists(privateKeyFilePath)) {
            try {
                Path newPrivateKeyPath = Files.createFile(privateKeyFilePath);
                Files.writeString(newPrivateKeyPath, asymmetricKeyPair.getPrivate().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return asymmetricKeyPair;
    }

}
