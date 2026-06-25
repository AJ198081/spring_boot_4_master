package dev.aj.stream_gatherers;

import org.springframework.boot.SpringApplication;

class StreamGatherersApplicationTests {

    static void main(String[] args) {
        SpringApplication.from(StreamGatherersApplication::main)
                .withAdditionalProfiles("test")
                .run(args);
    }

}
