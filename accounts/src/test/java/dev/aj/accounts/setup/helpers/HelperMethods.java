package dev.aj.accounts.setup.helpers;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@RequiredArgsConstructor
public class HelperMethods {

    private final Faker faker;

    public  <T extends Enum<T>> T getRandomEnumValue(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[faker.number().numberBetween(0, values.length)];
    }

}
