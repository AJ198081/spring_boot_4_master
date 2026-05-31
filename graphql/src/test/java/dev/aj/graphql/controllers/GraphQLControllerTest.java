package dev.aj.graphql.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class GraphQLControllerTest {

    private RestTestClient restTestClient;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient
//                .bindToController(new GraphQLController())
                .bindToApplicationContext(context)
                .build();
    }

    @Test
    void sayHello() {
        restTestClient.post()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }
}