package dev.aj.data_oriented_programming.practice.loans;

public interface LoanProcessor {

    default LoanStatus processLoan(LoanStatus loanStatus) {
        return switch (loanStatus) {
            case LoanStatus.Submitted submittedLoan -> this.handle(submittedLoan);
            case LoanStatus.UnderReview underReviewLoan -> this.handle(underReviewLoan);
            case LoanStatus.OfferPhase offerPhaseLoan -> this.handle(offerPhaseLoan);
            case LoanStatus.Approved approvedLoan -> approvedLoan;
            case LoanStatus.Rejected rejectedLoan -> rejectedLoan;
        };
    }

    LoanStatus handle(LoanStatus.Submitted loanStatus);
    LoanStatus handle(LoanStatus.UnderReview loanStatus);
    LoanStatus handle(LoanStatus.OfferPhase offerPhaseLoan);

}
