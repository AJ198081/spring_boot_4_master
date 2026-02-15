package dev.aj.accounts.setup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AccountsApplicationTests {

    @Test
    void contextLoads() {
    }

}
