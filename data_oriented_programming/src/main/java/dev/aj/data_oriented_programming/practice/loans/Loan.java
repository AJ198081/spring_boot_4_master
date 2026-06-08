package dev.aj.data_oriented_programming.practice.loans;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Loan.PersonalLoan.class, name = "PersonalLoan"),
        @JsonSubTypes.Type(value = Loan.PropertyLoan.class, name = "PropertyLoan"),
        @JsonSubTypes.Type(value = Loan.AutoLoan.class, name = "AutoLoan")
})
public sealed interface Loan {

    LoanApplication application();

    record PersonalLoan(LoanApplication application) implements Loan { }

    record PropertyLoan(LoanApplication application, Property property) implements Loan { }

    record AutoLoan(LoanApplication application, Vehicle vehicle) implements Loan { }

}
