package dev.aj.crypto_java.config;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@Slf4j
public class CryptoKeyManager {

    public static final String USER_DIR = "user.dir";

    private static final String PRIVATE_KEY_PEM_TYPE = "PRIVATE KEY";
    private static final String PUBLIC_KEY_PEM_TYPE = "PUBLIC KEY";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void persistRSAKeyPair(String dirName, String keyName) throws NoSuchAlgorithmException {

        Path privateKeyFilePath = keyFilePath(dirName, generateKeyPairNames(keyName).privateKeyName);
        Path publicKeyFilePath = keyFilePath(dirName, generateKeyPairNames(keyName).publicKeyName);

        if (isPresent(privateKeyFilePath) && isPresent(publicKeyFilePath)) {
            log.info("RSA key pair already present at {}", privateKeyFilePath.getParent());
            return;
        }

//     Assumption is if you are here, either private or public key are missing, delete and create new

        try {
            Files.deleteIfExists(privateKeyFilePath);
            Files.deleteIfExists(publicKeyFilePath);
        } catch (IOException e) {
            log.error("Error deleting existing RSA key pair", e);
            throw new RuntimeException(e);
        }

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

//     KeySize is the number of bits in the generated public/private key
        keyPairGenerator.initialize(2048);

        KeyPair asymmetricKeyPair = keyPairGenerator.generateKeyPair();

        log.info("Asymmetric key pair generated successfully");

        try {
            Files.createDirectories(privateKeyFilePath.getParent());
            // getEncoded() is PKCS#8 for private keys and X.509 SubjectPublicKeyInfo for public keys
            writePem(privateKeyFilePath, PRIVATE_KEY_PEM_TYPE, asymmetricKeyPair.getPrivate().getEncoded());
            writePem(publicKeyFilePath, PUBLIC_KEY_PEM_TYPE, asymmetricKeyPair.getPublic().getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPair readKeyPair(String dirName, String keyName) {

        try {
            KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);

            PublicKey extractedPublicKey = rsaKeyFactory.generatePublic(
                    new X509EncodedKeySpec(readPemContent(keyFilePath(dirName, generateKeyPairNames(keyName).publicKeyName), PUBLIC_KEY_PEM_TYPE)));
            PrivateKey extractedPrivateKey = rsaKeyFactory.generatePrivate(
                    new PKCS8EncodedKeySpec(readPemContent(keyFilePath(dirName, generateKeyPairNames(keyName).privateKeyName), PRIVATE_KEY_PEM_TYPE)));

            return new KeyPair(extractedPublicKey, extractedPrivateKey);
        } catch (GeneralSecurityException e) {
            log.error("Error while loading the RSA key pair, did you generate the key using the same implementation?");
            throw new RuntimeException(e);
        }
    }

    private static Path keyFilePath(String dirName, String fileName) {
        return Path.of(System.getProperty(USER_DIR), dirName).resolve(fileName);
    }

    private static boolean isPresent(Path path) {
        try {
            return Files.exists(path) && Files.size(path) > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writePem(Path path, String type, byte[] content) throws IOException {
        try (PemWriter pemWriter = new PemWriter(Files.newBufferedWriter(path))) {
            pemWriter.writeObject(new PemObject(type, content));
        }
    }

    private static byte[] readPemContent(Path path, String expectedType) {
        try (PemReader pemReader = new PemReader(Files.newBufferedReader(path))) {
            PemObject pemObject = pemReader.readPemObject();
            if (pemObject == null || !expectedType.equals(pemObject.getType())) {
                throw new IllegalStateException("Expected a '%s' PEM block in %s".formatted(expectedType, path));
            }
            return pemObject.getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    record KeyPairNames(String privateKeyName, String publicKeyName) {
    }

    private static KeyPairNames generateKeyPairNames(String keyName) {
        return new KeyPairNames(keyName + ".pem", keyName + "pub.pem");
    }
}
