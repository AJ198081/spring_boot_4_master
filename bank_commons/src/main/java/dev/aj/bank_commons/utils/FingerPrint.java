package dev.aj.bank_commons.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FingerPrint {
    private FingerPrint() {
    }

    public static <T> String generateFor(T requestObject) {

        String requestObject1 = requestObject.toString();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(requestObject1.getBytes(StandardCharsets.UTF_8));

            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : digest) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
