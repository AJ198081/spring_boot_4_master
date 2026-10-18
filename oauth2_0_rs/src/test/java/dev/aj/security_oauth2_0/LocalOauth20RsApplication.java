package dev.aj.security_oauth2_0;

import org.springframework.boot.SpringApplication;

class LocalOauth20RsApplication {

    static void main(String... args) {
        SpringApplication.from(Oauth20RsApplication::main)
                .withAdditionalProfiles("test")
                .run(args);
    }

}
