package dev.aj.data_oriented_programming.practice.loans;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
class LoanStatusTest {

    private static JsonMapper jsonMapper;

    @BeforeAll
    static void beforeAll() {
        jsonMapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }


    @Test
    void testSerialization() {

        LoanStatus.Submitted submittedLoanStatus = new LoanStatus.Submitted(new Loan.PersonalLoan(new LoanApplication(new Applicant("AJ B", 750, 75_000), new LoanTerm(15_000, DaysDuration.DAYS_THIRTY))));

        String json = jsonMapper.writeValueAsString(submittedLoanStatus);

        log.info("Serialized JSON:\n{}", json);

        LoanStatus deserializedLoanStatus = jsonMapper.readValue(json, LoanStatus.class);

        log.info("Deserialized Object's ToString:\n{}", deserializedLoanStatus);
    }

    @Test
    void testPropertyLoanSerialization() {
        LoanStatus.Submitted submittedLoanStatus = new LoanStatus.Submitted(
                new Loan.PropertyLoan(
                        new LoanApplication(new Applicant("AJ B", 750, 75_000),
                                new LoanTerm(15_000, DaysDuration.DAYS_THIRTY)),
                        new Property.Residential(
                                new Address("123 Street", "London", "Greater London", "E1 1AA"),
                                5)
                )
        );

        String json = jsonMapper.writeValueAsString(submittedLoanStatus);
        System.out.println(json);

        LoanStatus deserializedLoanStatus = jsonMapper.readValue(json, LoanStatus.class);
        System.out.println(deserializedLoanStatus);
    }

    @Test
    void testDeserialization() {
        String json = "{\"@type\":\"LoanStatus$Submitted\",\"loan\":{\"@type\":\"PersonalLoan\",\"application\":{\"applicant\":{\"name\":\"AJ B\",\"creditScore\":750,\"income\":75000},\"loanTerm\":{\"amount\":15000,\"days\":\"DAYS_THIRTY\"}}}}";

        LoanStatus deserializedLoanStatus = jsonMapper.readValue(json, LoanStatus.class);
        System.out.println(deserializedLoanStatus);
    }

}
