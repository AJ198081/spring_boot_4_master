package dev.aj.commons.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Slf4j
public class FingerPrint {
    private FingerPrint() {
    }

    public static <T> String generateFor(T requestObject) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] digest = md.digest(requestObject.toString().getBytes(StandardCharsets.UTF_8));

            return HexFormat.of()
                    .formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to find Message Digest Algorithm", e);
            throw new RuntimeException(e);
        }
    }

}
