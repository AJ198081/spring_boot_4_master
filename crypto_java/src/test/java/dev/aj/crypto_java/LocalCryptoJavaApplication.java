package dev.aj.crypto_java;

import org.springframework.boot.SpringApplication;

class LocalCryptoJavaApplication {

    static void main(String[] args) {
        SpringApplication.from(CryptoJavaApplication::main)
                .run(args);
    }

}
