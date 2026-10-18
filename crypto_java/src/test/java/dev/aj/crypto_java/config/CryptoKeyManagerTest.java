package dev.aj.crypto_java.config;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoKeyManagerTest {

    private final CryptoKeyManager cryptoKeyManager = new CryptoKeyManager();

    @Test
    public void testGenerateKeyPair() {
        KeyPair keyPair = cryptoKeyManager.readKeyPair("crypto_command_line/key_pairs", "test");
        assertNotNull(keyPair);
    }

}