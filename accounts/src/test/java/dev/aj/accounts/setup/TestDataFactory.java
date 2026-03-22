package dev.aj.accounts.setup;

import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AddressRequest;
import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.entities.enums.AccountType;
import dev.aj.accounts.common.domain.entities.enums.AddressType;
import dev.aj.accounts.setup.helpers.HelperMethods;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.env.Environment;

import java.util.stream.Stream;

@TestConfiguration(proxyBeanMethods = false)
@RequiredArgsConstructor
@SuppressWarnings({"unused", "unaccessed"})
public class TestDataFactory {

    private final Faker faker;
    private final Environment environment;
    private final HelperMethods helperMethods;

//  Customers
    public Stream<CustomerRequest> generateCustomerRequests() {
        return Stream.generate(() -> CustomerRequest.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .addresses(null)
                .build());
    }

    public CustomerRequest generateCustomerRequest() {
        return generateCustomerRequests()
                .findFirst()
                .orElseThrow();
    }


//   Addresses
    public Stream<AddressRequest> generateAddresses() {
        return Stream.generate(() -> AddressRequest.builder()
                .addressType(helperMethods.getRandomEnumValue(AddressType.class))
                .addressLine1(faker.address().streetAddress())
                .city(faker.address().city())
                .state(faker.address().state())
                .postCode(faker.address().zipCode())
                .country(faker.address().country())
                .build());
    }


//   Accounts
    public Stream<AccountRequest> generateAccounts() {
        return Stream.generate(() -> AccountRequest.builder()
                .bsb(
                        helperMethods.getRandomEnumValue(AustralianBanks.class).getBankBsb()
                                        .concat(faker.number().digits(4))
                )
                .accountNumber(faker.number().digits(10))
                .accountName(faker.name().fullName())
                .type(String.valueOf(helperMethods.getRandomEnumValue(AccountType.class)))
                .build());
    }

    public AddressRequest generateAnAddressRequest() {
        return generateAddresses()
                .findFirst()
                .orElseThrow();
    }

    public AccountRequest generateAnAccount() {
        return generateAccounts()
                .findFirst()
                .orElseThrow();
    }
}
