package dev.aj.data_oriented_programming.practice.loans;

public class LoanProcessorImpl implements LoanProcessor {

    @Override
    public LoanStatus handle(LoanStatus.Submitted loanStatus) {

        return new LoanStatus.UnderReview(loanStatus.loan());
    }

    @Override
    public LoanStatus handle(LoanStatus.UnderReview loanStatus) {

        return switch (loanStatus.loan()) {
            case Loan.PersonalLoan personalLoan -> this.handelPersonalLoanReview(personalLoan);
            case Loan.AutoLoan autoLoan -> this.handleAutoLoanReview(autoLoan);
            case Loan.PropertyLoan propertyLoan -> this.handlePropertyLoanReview(propertyLoan);
        };
    }

    @Override
    public LoanStatus handle(LoanStatus.OfferPhase offerPhaseLoan) {

        double interestRate = switch (offerPhaseLoan.loan()) {
            case Loan.PersonalLoan _ -> 0.20;
            case Loan.AutoLoan _ -> 0.07;
            case Loan.PropertyLoan(_, Property.Residential(_, int rooms)) -> {
                if (rooms < 4) {
                    yield 0.05;
                }
                yield 0.06;
            }
            case Loan.PropertyLoan(_, Property.Commercial(_, BusinessType businessType)) ->
                    businessType.equals(BusinessType.OFFICE)
                    ? 0.07
                    : 0.09;
        };

        return new LoanStatus.Approved(offerPhaseLoan.loan(), interestRate);
    }

    private LoanStatus handlePropertyLoanReview(Loan.PropertyLoan propertyLoan) {

        Property property = propertyLoan.property();
        Applicant applicant = propertyLoan.application().applicant();

        switch (property) {

            case Property.Residential residentialProperty -> {
                if (applicant.income() < 65000) {
                    return new LoanStatus.Rejected(propertyLoan, "Applicant is under 18 years old");
                }
                if (residentialProperty.rooms() < 2) {
                    return new LoanStatus.Rejected(propertyLoan, "The property has less than 2 rooms");
                }
                return new LoanStatus.OfferPhase(propertyLoan);
            }

            case Property.Commercial commercialProperty -> {
                if (applicant.income() < 100000) {
                    return new LoanStatus.Rejected(propertyLoan, "Applicant is under 18 years old");
                }

                if (commercialProperty.businessType().equals(BusinessType.OFFICE)) {
                    return new LoanStatus.Rejected(propertyLoan, "Office type property");
                }

                return new LoanStatus.OfferPhase(propertyLoan);
            }
        }
    }

    private LoanStatus handleAutoLoanReview(Loan.AutoLoan autoLoan) {
        return switch (autoLoan.vehicle()) {
            case Vehicle.Car car -> {
                if (car.make().equals("Toyota")) {
                    yield new LoanStatus.Rejected(autoLoan, "Toyota vehicles are not eligible for auto loans");
                }
                if (autoLoan.application().applicant().creditScore() < 750) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's credit score is too low");
                }
                if (autoLoan.application().applicant().income() < 100000) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's income is too low");
                }
                yield new LoanStatus.OfferPhase(autoLoan);
            }

            case Vehicle.Motorcycle motorcycle -> {
                if (autoLoan.application().applicant().creditScore() < 600) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's credit score is too low");
                }
                if (autoLoan.application().applicant().income() < 50000) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's income is too low");
                }
                yield new LoanStatus.OfferPhase(autoLoan);
            }
        };
    }


    private LoanStatus handelPersonalLoanReview(Loan.PersonalLoan personalLoan) {
        Applicant personalLoanApplicant = personalLoan.application().applicant();
        LoanTerm loanTerm = personalLoan.application().loanTerm();

        if (personalLoanApplicant.creditScore() < 500 || personalLoanApplicant.income() < 50000) {
            return new LoanStatus.Rejected(personalLoan, "Applicant's credit score or income is too low");
        }

        switch (loanTerm.days()) {
            case DAYS_THIRTY:
                if (loanTerm.amount() > 20_000) {
                    return new LoanStatus.Rejected(personalLoan, "Loan amount is too high");
                }
            case DAYS_SIXTY:
            case DAYS_NINETY:
                if (loanTerm.amount() > 40_000) {
                    return new LoanStatus.Rejected(personalLoan, "Loan amount is too high");
                }
            case DAYS_ONE_EIGHTY:
            case DAYS_THREE_HUNDRED_AND_SIXTY_FIVE:
                if (loanTerm.amount() > 60_000) {
                    return new LoanStatus.Rejected(personalLoan, "Loan amount is too high");
                }
        }

        return new LoanStatus.OfferPhase(personalLoan);
    }
}
