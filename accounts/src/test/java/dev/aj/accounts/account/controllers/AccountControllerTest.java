package dev.aj.accounts.account.controllers;

import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.setup.TestConfig;
import dev.aj.accounts.setup.TestDataFactory;
import dev.aj.accounts.setup.helpers.HelperMethods;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(value = {TestConfig.class, TestDataFactory.class, HelperMethods.class})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTest {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private Environment environment;

    @LocalServerPort
    private int port;

    private RestClient accountRestClient;

    @BeforeAll
    void beforeAll() {
        accountRestClient = testConfig.restClient(
                "http://localhost:%d%s".formatted(
                        port,
                        environment.getRequiredProperty("api.basePath.accounts", String.class)));
    }

    @AfterAll
    void afterAll() {
        accountRestClient = null;
    }

    @Nested
    @Order(1)
    class createAccount {

        @Test
        void createAccountSuccessfully() {

            AccountRequest randomAccountRequest = testDataFactory.generateAnAccount();

            ResponseEntity<Void> createAccountResponse = accountRestClient.post()
                    .uri("/")
                    .body(randomAccountRequest)
                    .retrieve()
                    .toBodilessEntity();

            Assertions.assertThat(createAccountResponse).isNotNull()
                    .satisfies(response -> Assertions.assertThat(response.getStatusCode()).isNotNull())
                    .extracting(ResponseEntity::getHeaders)
                    .extracting(HttpHeaders::getLocation)
                    .isNotNull()
                    .satisfies(location -> Assertions.assertThat(location)
                            .isNotNull().asString()
                            .contains("/accounts/"));
        }


    }

    @Test
    void getAccount() {
    }

    @Test
    void replaceAccount() {
    }

    @Test
    void updateAccount() {
    }
}