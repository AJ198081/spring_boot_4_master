package dev.aj.data_oriented_programming.practice.loans;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.extern.slf4j.Slf4j;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = LoanStatus.Submitted.class, name = "Submitted"),
        @JsonSubTypes.Type(value = LoanStatus.UnderReview.class, name = "UnderReview"),
        @JsonSubTypes.Type(value = LoanStatus.OfferPhase.class, name = "OfferPhase"),
        @JsonSubTypes.Type(value = LoanStatus.Approved.class, name = "Approved"),
        @JsonSubTypes.Type(value = LoanStatus.Rejected.class, name = "Rejected")
})
public sealed interface LoanStatus {

    Loan loan();

    record Submitted(Loan loan) implements LoanStatus { }
    record UnderReview(Loan loan) implements LoanStatus { }
    record OfferPhase(Loan loan) implements LoanStatus { }
    record Approved(Loan loan, double interestRate) implements LoanStatus { }
    record Rejected(Loan loan, String reason) implements LoanStatus { }

}

@Slf4j
record InterestRate(double rate) {
    public InterestRate {

        if (rate < 0 ) {
            throw new IllegalArgumentException("Interest rate can't be negative");
        }

        if (rate > 1) {
            log.warn("Interest rate {} is greater than 1, normalizing to between 0 and 1", rate);
            rate = rate / 100;
        }
    }
}
