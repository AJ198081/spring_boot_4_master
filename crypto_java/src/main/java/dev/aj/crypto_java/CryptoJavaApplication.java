package dev.aj.crypto_java;

import dev.aj.crypto_java.config.CryptoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootApplication
@Slf4j
public class CryptoJavaApplication implements ApplicationListener<ApplicationStartedEvent> {


    private static final String CRYPTO_KEY_DIRECTORY = "crypto_command_line/key_pairs";
    private static final String CERTIFICATE_SIGNING_REQUEST_DIRECTORY = "crypto_command_line/csr";
    private static final String ISSUER_KEY_NAME = "rsa-root";
    private static final String SUBJECT_KEY_NAME = "rsa_subject";

    public static final String CERTIFICATE_SIGNING_REQUEST_FILE_NAME = "csr_for_developer";

    static void main(String[] args) {
        SpringApplication.run(CryptoJavaApplication.class, args);

    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        CryptoManager cryptoKeyManager = event.getApplicationContext()
                .getBean(CryptoManager.class);

//        Generate a new KeyPair for the Issuer (think root CA)
        try {
            cryptoKeyManager.assureRSAKeyPair(CRYPTO_KEY_DIRECTORY, ISSUER_KEY_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        KeyPair issuerKeyPair = cryptoKeyManager.readKeyPair(CRYPTO_KEY_DIRECTORY, ISSUER_KEY_NAME);

        assertNotNull(issuerKeyPair);

//        Generate a self-signed root CA certificate
        X509Certificate rootCertificate = cryptoKeyManager.generateSelfSignedV3X509Certificate(
                issuerKeyPair,
                new CryptoManager.DistinguishedName(
                        "developer-world.com.au",
                        "aj-dev",
                        "engineering",
                        "Sydney",
                        "NSW",
                        "AU")
        );

        assertNotNull(rootCertificate);

//        Can use https://certlogik.com/ to validate the certificates
        log.info("Generated root certificate -\n{}", cryptoKeyManager.base64EncodeX509Certificate(rootCertificate));

//        Now, generate a keypair for the 'subject', or the intermediate CA
        try {
            cryptoKeyManager.assureRSAKeyPair(CRYPTO_KEY_DIRECTORY, SUBJECT_KEY_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        KeyPair subjectKeyPair = cryptoKeyManager.readKeyPair(CRYPTO_KEY_DIRECTORY, SUBJECT_KEY_NAME);

//        Generate and persist 'Certificate Sign Request' by, and using the subject's keypair
        Path pathToPersistCSR = Path.of(CERTIFICATE_SIGNING_REQUEST_DIRECTORY, CERTIFICATE_SIGNING_REQUEST_FILE_NAME);
        cryptoKeyManager.generateCertificateSigningRequest(
                subjectKeyPair,
                new CryptoManager.DistinguishedName(
                        "client.com.au",
                        "client-dev",
                        "engineering",
                        "Melbourne",
                        "VIC",
                        "AU"),
                pathToPersistCSR);

        log.debug("Generated CertificateSignRequest, and persisted to [{}]", pathToPersistCSR);

//      Now, as the Root CA sign the CSR
        X509Certificate signedCertificate = cryptoKeyManager.generateSignedCertificate(pathToPersistCSR, rootCertificate, issuerKeyPair);
        assertNotNull(signedCertificate);

//      Can use https://certlogik.com/ to validate the certificates
        log.info("Generated signed certificate for the subject - \n{}", cryptoKeyManager.base64EncodeX509Certificate(signedCertificate));

        try {
            signedCertificate.verify(rootCertificate.getPublicKey());
        } catch (CertificateException
                 | NoSuchAlgorithmException
                 | InvalidKeyException
                 | NoSuchProviderException
                 | SignatureException e) {
            log.error("Tried but failed to verify the signed certificate with Root CA's Public Key, review the following exception to find out the why");
            throw new RuntimeException(e);
        }

        log.info("Verified signed certificate for the subject with Root CA's Public Key");

        try {
            signedCertificate.verify(subjectKeyPair.getPublic());
        } catch (CertificateException
                 | NoSuchAlgorithmException
                 | InvalidKeyException
                 | NoSuchProviderException
                 | SignatureException e) {
            if (e instanceof SignatureException) {
                log.error("Tried but as expected failed to verify the signed certificate with Subject's Public Key, because it was signed using Root CA's private key");
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}
