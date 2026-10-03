package dev.aj.crypto_java.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Component
@Slf4j
public class CryptoKeyManager {

    public static final String USER_DIR = "user.dir";
    public static final String CRYPTO_COMMAND_LINE_DIR = "crypto_command_line";
    public static final String RSA_PRIVATE_KEY_FILE = "aj-rsa.pem";

    @PostConstruct
    public KeyPair persistRSAKeyPair() throws NoSuchAlgorithmException {

        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

//     KeySize is the number of bits in the generated public/private key
        keyPairGenerator.initialize(2048);

        KeyPair asymmetricKeyPair = keyPairGenerator.generateKeyPair();

        log.info("Asymmetric key pair generated successfully");
        log.debug("Public key: {}", asymmetricKeyPair.getPublic());
        log.debug("Private key: {}", asymmetricKeyPair.getPrivate());

        Path privateKeyFilePath = Path.of(System.getProperty(USER_DIR), CRYPTO_COMMAND_LINE_DIR)
                .resolve(RSA_PRIVATE_KEY_FILE);

        if (Files.notExists(privateKeyFilePath)) {
            try {
                Path newPrivateKeyPath = Files.createFile(privateKeyFilePath);
                Files.writeString(newPrivateKeyPath, (CharSequence) asymmetricKeyPair.getPrivate());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return asymmetricKeyPair;
    }

    public KeyPair getPublicKeyCertification() {

        KeyFactory rsaKeyFactory;
        try {
            rsaKeyFactory = KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

        PublicKey extractedPublicKey;
        try {
            extractedPublicKey = rsaKeyFactory.generatePublic(readKeySpecsFromTheDisc());
        } catch (InvalidKeySpecException e) {
            log.error("Error while generating a public key, did you generate the key using the same implementation?");
            throw new RuntimeException(e);
        }
        PrivateKey extractedPrivateKey;
        try {
            extractedPrivateKey = rsaKeyFactory.generatePrivate(readKeySpecsFromTheDisc());
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        log.debug("Generated key - \n public {}, and \n private {}", extractedPublicKey, extractedPrivateKey);

        return new KeyPair(extractedPublicKey, extractedPrivateKey);
    }

    private X509EncodedKeySpec readKeySpecsFromTheDisc() {
        Path privateKeyFilePath = Path.of(System.getProperty(USER_DIR), CRYPTO_COMMAND_LINE_DIR)
                .resolve(RSA_PRIVATE_KEY_FILE);
        try {
            PemReader pemReader = new PemReader(new FileReader(privateKeyFilePath.toFile()));
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();

            return new X509EncodedKeySpec(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
