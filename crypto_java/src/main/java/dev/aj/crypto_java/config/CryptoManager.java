package dev.aj.crypto_java.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class CryptoManager {

    public static final String USER_DIR = "user.dir";

    private static final String PRIVATE_KEY = "PRIVATE KEY";
    private static final String PUBLIC_KEY = "PUBLIC KEY";
    private static final String CERTIFICATE_REQUEST = "CERTIFICATE REQUEST";
    public static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void assureRSAKeyPair(String dirName, String keyName) throws NoSuchAlgorithmException {

        KeyPairNames keyPairNames = generateKeyPairNames(keyName);

        Path privateKeyFilePath = resolvePath(dirName, keyPairNames.privateKeyName);
        Path publicKeyFilePath = resolvePath(dirName, keyPairNames.publicKeyName);

        if (isFileValid(privateKeyFilePath) && isFileValid(publicKeyFilePath)) {
            log.info("RSA key pair '{}' already present at {}", keyName, privateKeyFilePath.getParent());
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
            writePem(privateKeyFilePath, PRIVATE_KEY, asymmetricKeyPair.getPrivate().getEncoded());
            writePem(publicKeyFilePath, PUBLIC_KEY, asymmetricKeyPair.getPublic().getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPair readKeyPair(String dirName, String keyName) {

        KeyPairNames keyPairNames = generateKeyPairNames(keyName);

        try {
            KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);


            // X509EncodedKeySpec is the specification for the Public Key
            PublicKey extractedPublicKey = rsaKeyFactory.generatePublic(
                    new X509EncodedKeySpec(
                            readPemContent(
                                    resolvePath(dirName, keyPairNames.publicKeyName),
                                    PUBLIC_KEY)
                    ));

            // PKCS8EncodedKeySpec is the specification for the Private Key
            PrivateKey extractedPrivateKey = rsaKeyFactory.generatePrivate(
                    new PKCS8EncodedKeySpec(
                            readPemContent(
                                    resolvePath(dirName, keyPairNames.privateKeyName),
                                    PRIVATE_KEY)
                    ));

            return new KeyPair(extractedPublicKey, extractedPrivateKey);
        } catch (GeneralSecurityException e) {
            log.error("Error while loading the RSA key pair, did you generate the key using the same implementation?");
            throw new RuntimeException(e);
        }
    }

    public X509Certificate generateSelfSignedV3X509Certificate(KeyPair keyPair, DistinguishedName distinguishedName) {

        Calendar calendar = Calendar.getInstance();
        Date notBefore = calendar.getTime();

        calendar.add(Calendar.YEAR, 1);
        Date notAfter = calendar.getTime();

        X500Name x500Name = new X500Name(distinguishedName.toX500FormattedString());

        //  For root CA the issue and the subjects are the same
        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                x500Name,
                BigInteger.valueOf(System.currentTimeMillis()),
                notBefore,
                notAfter,
                x500Name,
                keyPair.getPublic()
        );

        return generateX509Certificate(keyPair, certificateBuilder);
    }

    private static X509Certificate generateX509Certificate(KeyPair keyPair, X509v3CertificateBuilder certificateBuilder) {
        try {
            JcaX509ExtensionUtils jcaX509ExtensionUtils = new JcaX509ExtensionUtils();

            certificateBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
            certificateBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign));
            certificateBuilder.addExtension(Extension.subjectKeyIdentifier, false, jcaX509ExtensionUtils.createSubjectKeyIdentifier(keyPair.getPublic()));
            certificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, jcaX509ExtensionUtils.createAuthorityKeyIdentifier(keyPair.getPublic()));

            JcaContentSignerBuilder certificateSignerBuilder = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM);

            return new JcaX509CertificateConverter()
                    .getCertificate(certificateBuilder
                            .build(certificateSignerBuilder.build(keyPair.getPrivate())));

        } catch (NoSuchAlgorithmException | CertIOException | OperatorCreationException | CertificateException e) {
            log.error("Error creating certificate extensions", e);
            throw new RuntimeException(e);
        }
    }

    public void generateCertificateSigningRequest(KeyPair subjectKeyPair, DistinguishedName subjectDistinguishedName, Path pathToPersistCSR) {

//        Note - everything in the CSR is in plain text, nothing is encrypted, contains the public key of the requester and attributes it wants to be certified
        JcaPKCS10CertificationRequestBuilder certificationRequestBuilder = new JcaPKCS10CertificationRequestBuilder(subjectDistinguishedName.toX500Name(), subjectKeyPair.getPublic());

        GeneralNames subjectAlternativeNames = new GeneralNames(new GeneralName[]{
                new GeneralName(GeneralName.dNSName, subjectDistinguishedName.commonName),
                new GeneralName(GeneralName.dNSName, subjectDistinguishedName.organization.concat(".com"))}
        );

        ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();

        try {
            extensionsGenerator.addExtension(Extension.subjectAlternativeName, false, subjectAlternativeNames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        certificationRequestBuilder.addAttribute(
                PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
                extensionsGenerator.generate()
        );

        PKCS10CertificationRequest certificationRequest;

        try {
//          The CSR itself is signed purely to assure to the CA that the requester holds the private key for this request
            ContentSigner contentSigner = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
                    .build(subjectKeyPair.getPrivate());

            certificationRequest = certificationRequestBuilder.build(contentSigner);
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        }

        try {
            writePem(pathToPersistCSR, CERTIFICATE_REQUEST, certificationRequest.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public X509Certificate generateSignedCertificate(Path locationOfCSR, X509Certificate issuer, KeyPair issuerKeyPair) {

        PKCS10CertificationRequest certificationRequest = convertToPKCS10(locationOfCSR);

        X500Name subject = Objects.requireNonNull(certificationRequest, "Certification Request can't be null")
                .getSubject();

        SubjectPublicKeyInfo subjectPublicKeyInfo = certificationRequest.getSubjectPublicKeyInfo();

        return generateCertificate(issuer, issuerKeyPair, subjectPublicKeyInfo, subject);
    }

    private X509Certificate generateCertificate(X509Certificate issuer, KeyPair issuerKeyPair, SubjectPublicKeyInfo subjectPublicKeyInfo, X500Name subject) {

        Calendar calendar = Calendar.getInstance();
        Date notBefore = calendar.getTime();

        calendar.add(Calendar.YEAR, 1);
        Date notAfter = calendar.getTime();

        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                X500Name.getInstance(issuer.getSubjectX500Principal().getEncoded()),
                BigInteger.valueOf(System.currentTimeMillis()),
                notBefore,
                notAfter,
                subject,
                subjectPublicKeyInfo
        );

        return generateX509Certificate(issuerKeyPair, certificateBuilder);
    }

    private PKCS10CertificationRequest convertToPKCS10(Path locationOfCSR) {

        try (BufferedReader bufferedReader = Files.newBufferedReader(locationOfCSR)) {
            PEMParser pemParser = new PEMParser(bufferedReader);
            Object parsedObject = pemParser.readObject();
            if (parsedObject instanceof PKCS10CertificationRequest certificationRequest) {
                return certificationRequest;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @SneakyThrows
    public String base64EncodeX509Certificate(X509Certificate certificate) {
        return "-----BEGIN CERTIFICATE-----" +
                Base64.getEncoder().encodeToString(certificate.getEncoded()) +
                "-----END CERTIFICATE-----";
    }

    public static Path resolvePath(String dirName, String fileName) {
        //  Path will be resolved relative to the current working directory for the application
        String currentDirectory = System.getProperty(USER_DIR);

        return Path.of(currentDirectory, dirName)
                .resolve(fileName);
    }

    public static Optional<String> extractFileExtension(Path path) {

        String fileName = path.getFileName().toString();

        if (fileName.lastIndexOf(".") > 0) {
            return Optional.of(fileName.substring(fileName.lastIndexOf(".") + 1));
        }

        return Optional.empty();
    }

    private static boolean isFileValid(Path filePath) {
        try {
            return Files.exists(filePath) && Files.size(filePath) > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writePem(Path path, String type, byte[] content) throws IOException {

        if (!isFileValid(path)) {
            Files.createDirectories(path.getParent());
        }

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

    public record DistinguishedName(String commonName, String organization, String organizationalUnit, String locality,
                                    String state, String country) {

        public String toX500FormattedString() {
            return """
                    CN=%s, O=%s, OU=%s, L=%s, ST=%s, C=%s
                    """.formatted(commonName, organization, organizationalUnit, locality, state, country);
        }

        public X500Name toX500Name() {
            return new X500Name(toX500FormattedString());
        }
    }

    private static KeyPairNames generateKeyPairNames(String keyName) {
        return new KeyPairNames(keyName + ".pem", keyName + "pub.pem");
    }
}
