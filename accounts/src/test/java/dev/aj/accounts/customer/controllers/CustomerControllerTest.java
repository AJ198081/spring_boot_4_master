package dev.aj.accounts.customer.controllers;

import dev.aj.accounts.setup.TestConfig;
import dev.aj.accounts.setup.TestDataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(value = {TestConfig.class, TestDataFactory.class})
@TestPropertySource(locations = {"classpath: application-test.yaml"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCustomer() {
    }

    @Test
    void getCustomer() {
    }
}