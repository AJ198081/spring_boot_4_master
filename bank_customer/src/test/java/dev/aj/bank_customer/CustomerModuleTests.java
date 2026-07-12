package dev.aj.bank_customer;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.TestPropertySource;

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CustomerModuleTests {

    ApplicationModules modules = ApplicationModules.of(BankCustomerApplication.class);

    @Test
    void testModularity() {
        modules.verify();
    }

    @Test
    void createDocumentation() {
        new Documenter(modules).writeModulesAsPlantUml();
    }
}
