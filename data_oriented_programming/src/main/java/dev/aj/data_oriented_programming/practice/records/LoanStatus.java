package dev.aj.data_oriented_programming.practice.records;

public sealed interface LoanStatus {

    Loan loan();

    record Submitted(Loan loan) implements LoanStatus { }
    record UnderReview(Loan loan) implements LoanStatus { }
    record offerPhase(Loan loan) implements LoanStatus { }
    record Approved(Loan loan, double interestRate) implements LoanStatus { }
    record Rejected(Loan loan, String reason) implements LoanStatus { }

}
