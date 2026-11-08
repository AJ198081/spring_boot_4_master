package dev.aj.crypto_java.config;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringJUnitConfig(CryptoManager.class)
public class CryptoKeyManagerTest {

    private static final String KEY_PAIR_DIRECTORY = "crypto_command_line/key_pairs";
    private static final String CERTIFICATE_KEY_NAME = "certificate_key";
    private static final String ROOT_CA_KEY_NAME = "root_ca_key";
    private static final String SUBJECT_KEY_NAME = "client_key";

    private static final CryptoManager.DistinguishedName ROOT_CA_DN = new CryptoManager.DistinguishedName(
            "digicert-root-ca", "digicert-root", "Engineering", "Sydney", "NSW", "AU");

    private static final CryptoManager.DistinguishedName SUBJECT_DN = new CryptoManager.DistinguishedName(
            "client.com.au", "client-dev", "Engineering", "Melbourne", "VIC", "AU");

    @Autowired
    private CryptoManager cryptoKeyManager;

    @Test
    public void testGenerateKeyPair() throws NoSuchAlgorithmException {

        log.info("Ensuring RSA key pair...");

        cryptoKeyManager.assureRSAKeyPair(KEY_PAIR_DIRECTORY, CERTIFICATE_KEY_NAME);

        KeyPair keyPair = cryptoKeyManager.readKeyPair(KEY_PAIR_DIRECTORY, CERTIFICATE_KEY_NAME);

        assertNotNull(keyPair);

        CryptoManager.DistinguishedName distinguishedName = new CryptoManager.DistinguishedName(
                "Financial Industry",
                "www.dev.aj",
                "Engineering",
                "Sydney",
                "NSW",
                "AU"
        );

        X509Certificate x509Certificate = cryptoKeyManager.generateSelfSignedV3X509Certificate(
                keyPair,
                distinguishedName
        );

        log.info("Generated root certificate: {}", x509Certificate);

        log.info("Base64 encoded root certificate: {}", cryptoKeyManager.base64EncodeX509Certificate(x509Certificate));

        log.info("You can copy this base64 encoded certificate and try it in www.certlogik.com");
    }

    @Test
    void testSelfSignedRootCACertificate() throws Exception {

        cryptoKeyManager.assureRSAKeyPair(KEY_PAIR_DIRECTORY, ROOT_CA_KEY_NAME);
        KeyPair rootKeyPair = cryptoKeyManager.readKeyPair(KEY_PAIR_DIRECTORY, ROOT_CA_KEY_NAME);

        X509Certificate rootCertificate = cryptoKeyManager.generateSelfSignedV3X509Certificate(rootKeyPair, ROOT_CA_DN);

        assertNotNull(rootCertificate);
        assertEquals(3, rootCertificate.getVersion(), "Should be an X.509 v3 certificate");
        assertEquals("SHA256WITHRSA", rootCertificate.getSigAlgName().toUpperCase());

        // Self-signed: subject and issuer are the same, and match the requested DN
        assertEquals(rootCertificate.getSubjectX500Principal(), rootCertificate.getIssuerX500Principal());
        assertEquals(ROOT_CA_DN.toX500Name(), new JcaX509CertificateHolder(rootCertificate).getSubject());

        // Certificate carries the root's public key
        assertEquals(rootKeyPair.getPublic(), rootCertificate.getPublicKey());

        // Currently valid, for roughly one year
        assertDoesNotThrow(() -> rootCertificate.checkValidity());
        assertTrue(rootCertificate.getNotAfter().after(rootCertificate.getNotBefore()));

        // CA extensions: basicConstraints CA=true (-1 means "not a CA"), keyUsage includes keyCertSign (index 5)
        assertTrue(rootCertificate.getBasicConstraints() >= 0, "Root certificate should be a CA");
        assertNotNull(rootCertificate.getKeyUsage());
        assertTrue(rootCertificate.getKeyUsage()[5], "Root certificate should allow keyCertSign");

        // Self-signed: verifies with its own public key
        assertDoesNotThrow(() -> rootCertificate.verify(rootKeyPair.getPublic()));

        log.info("Self-signed root CA certificate -\n{}", cryptoKeyManager.base64EncodeX509Certificate(rootCertificate));
    }

    @Test
    void testRootCASignedSubjectCertificate(@TempDir Path tempDir) throws Exception {

        // Root CA
        cryptoKeyManager.assureRSAKeyPair(KEY_PAIR_DIRECTORY, ROOT_CA_KEY_NAME);
        KeyPair rootKeyPair = cryptoKeyManager.readKeyPair(KEY_PAIR_DIRECTORY, ROOT_CA_KEY_NAME);
        X509Certificate rootCertificate = cryptoKeyManager.generateSelfSignedV3X509Certificate(rootKeyPair, ROOT_CA_DN);

        // Subject key pair and CSR
        cryptoKeyManager.assureRSAKeyPair(KEY_PAIR_DIRECTORY, SUBJECT_KEY_NAME);
        KeyPair subjectKeyPair = cryptoKeyManager.readKeyPair(KEY_PAIR_DIRECTORY, SUBJECT_KEY_NAME);

        Path csrPath = tempDir.resolve("subject.csr");
        cryptoKeyManager.generateCertificateSigningRequest(subjectKeyPair, SUBJECT_DN, csrPath);

        assertTrue(Files.exists(csrPath) && Files.size(csrPath) > 0, "CSR should be persisted");
        assertTrue(Files.readString(csrPath).contains("-----BEGIN CERTIFICATE REQUEST-----"));

        // Root CA signs the CSR
        X509Certificate signedCertificate = cryptoKeyManager.generateSignedCertificate(csrPath, rootCertificate, rootKeyPair);

        assertNotNull(signedCertificate);
        assertEquals(3, signedCertificate.getVersion());

        // Issued by the root, for the subject
        assertEquals(rootCertificate.getSubjectX500Principal(), signedCertificate.getIssuerX500Principal());

        assertEquals(SUBJECT_DN.toX500Name(), new JcaX509CertificateHolder(signedCertificate).getSubject());
        assertNotEquals(signedCertificate.getSubjectX500Principal(), signedCertificate.getIssuerX500Principal());

        // Carries the subject's public key (taken from the CSR), not the Root CA's
        assertEquals(subjectKeyPair.getPublic(), signedCertificate.getPublicKey());
        assertNotEquals(rootKeyPair.getPublic(), signedCertificate.getPublicKey());

        assertDoesNotThrow(() -> signedCertificate.checkValidity());

        // Verifies with the root CA's public key ...
        assertDoesNotThrow(() -> signedCertificate.verify(rootCertificate.getPublicKey()));

        // ... but not with the subject's own public key, since it was signed with the root's private key
        assertThrows(SignatureException.class, () -> signedCertificate.verify(subjectKeyPair.getPublic()));

        log.info("Root CA signed subject certificate -\n{}", cryptoKeyManager.base64EncodeX509Certificate(signedCertificate));
    }
}