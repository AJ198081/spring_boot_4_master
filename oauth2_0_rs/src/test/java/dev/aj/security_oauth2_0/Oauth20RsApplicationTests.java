package dev.aj.security_oauth2_0;

import org.springframework.boot.SpringApplication;

class Oauth20RsApplicationTests {

    static void main(String... args) {
        SpringApplication.from(Oauth20RsApplication::main)
                .withAdditionalProfiles("test")
                .run(args);
    }

}
