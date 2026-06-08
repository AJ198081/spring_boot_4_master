package dev.aj.data_oriented_programming.practice.loans;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class LoanStatusTest {



    @Test
    void testSerialization() {

        JsonMapper jsonMapper = new JsonMapper();

        LoanStatus.Submitted submittedLoanStatus = new LoanStatus.Submitted(new Loan.PersonalLoan(new LoanApplication(new Applicant("AJ B", 750, 75_000), new LoanTerm(15_000, DaysDuration.DAYS_THIRTY))));

        String json = jsonMapper.writeValueAsString(submittedLoanStatus);

        System.out.println(json);

        LoanStatus deserializedLoanStatus = jsonMapper.readValue(json, LoanStatus.class);

        System.out.println(deserializedLoanStatus);

    }

    @Test
    void testPropertyLoanSerialization() {
        JsonMapper jsonMapper = new JsonMapper();

        LoanStatus.Submitted submittedLoanStatus = new LoanStatus.Submitted(
                new Loan.PropertyLoan(
                        new LoanApplication(new Applicant("AJ B", 750, 75_000), new LoanTerm(15_000, DaysDuration.DAYS_THIRTY)),
                        new Property.Residential(new Address("123 Street", "London", "Greater London", "E1 1AA"), 5)
                )
        );

        String json = jsonMapper.writeValueAsString(submittedLoanStatus);
        System.out.println(json);

        LoanStatus deserializedLoanStatus = jsonMapper.readValue(json, LoanStatus.class);
        System.out.println(deserializedLoanStatus);
    }

}