package dev.aj.data_oriented_programming.practice.loans;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@Slf4j
class LoanProcessorImplTest {

    @Test
    void testPropertyLoanProcessingFlow() {
//        LoanStatus newLoan = new LoanStatus.Submitted(new Loan.PersonalLoan(new LoanApplication(new Applicant("AJ B", 750, 75_000), new LoanTerm(15_000, DaysDuration.DAYS_THIRTY))));
        LoanApplication propertyLoanApplication = new LoanApplication(new Applicant("AJ B", 750, 75_000), new LoanTerm(30000, DaysDuration.DAYS_THREE_HUNDRED_AND_SIXTY_FIVE));
        Loan.PropertyLoan propertyLoan = new Loan.PropertyLoan(propertyLoanApplication, new Property.Residential(new Address("45 Lion Street", "Sydney", "NSW", "2000"), 5));
        LoanStatus newLoan = new LoanStatus.Submitted(propertyLoan);

        assertInstanceOf(LoanStatus.Approved.class, processUntilApprovedOrRejected(newLoan));
    }


    @Test
    void testPersonalLoanProcessingFlow() {
        LoanStatus newLoan = new LoanStatus.Submitted(new Loan.PersonalLoan(new LoanApplication(new Applicant("AJ B", 750, 75_000), new LoanTerm(15_000, DaysDuration.DAYS_THIRTY))));

        assertInstanceOf(LoanStatus.Approved.class, processUntilApprovedOrRejected(newLoan));
    }

    @Test
    void testAutoLoanProcessingFlow() {
        LoanStatus.Submitted submitted = new LoanStatus.Submitted(new Loan.AutoLoan(new LoanApplication(new Applicant("AJ B", 750, 75_000), new LoanTerm(15_000, DaysDuration.DAYS_THIRTY)), new Vehicle.Car("Toyota", "Camry", 2020)));

        assertInstanceOf(LoanStatus.Rejected.class, processUntilApprovedOrRejected(submitted));
    }

    private static LoanStatus processUntilApprovedOrRejected(@NonNull LoanStatus newLoan) {
        log.info("Processing loan,\n Status: {} \n", newLoan);

        while (!(newLoan instanceof LoanStatus.Approved || newLoan instanceof LoanStatus.Rejected)) {
            newLoan = new LoanProcessorImpl().processLoan(newLoan);
        }

        log.info("Loan processed,\n Status: {}", newLoan);

        return newLoan;
    }

}