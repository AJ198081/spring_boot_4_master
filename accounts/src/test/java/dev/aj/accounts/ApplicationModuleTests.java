package dev.aj.accounts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.ActiveProfiles;

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ApplicationModuleTests {

    @Test
    void verifyApplicationModules() {
        ApplicationModules.of(AccountsApplication.class).verify();
    }

    @Test
    void writeApplicationDocumentation() {

        new Documenter(ApplicationModules.of(AccountsApplication.class))
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }
}
