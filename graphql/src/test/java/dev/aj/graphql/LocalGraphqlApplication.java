package dev.aj.graphql;

import org.springframework.boot.SpringApplication;

public class LocalGraphqlApplication {

    static void main(String[] args) {
        SpringApplication.from(GraphqlApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
