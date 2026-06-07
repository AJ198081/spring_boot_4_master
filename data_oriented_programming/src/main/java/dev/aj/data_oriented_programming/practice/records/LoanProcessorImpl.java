package dev.aj.data_oriented_programming.practice.records;

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
                return new LoanStatus.offerPhase(propertyLoan);
            }

            case Property.Commercial commercialProperty -> {
                if (applicant.income() < 100000) {
                    return new LoanStatus.Rejected(propertyLoan, "Applicant is under 18 years old");
                }

                if (commercialProperty.businessType().equals(BusinessType.OFFICE)) {
                    return new LoanStatus.Rejected(propertyLoan, "Office type property");
                }

                return new LoanStatus.offerPhase(propertyLoan);
            }

        }
    }

    @Override
    public LoanStatus handle(LoanStatus.offerPhase offerPhaseLoan) {

        switch (offerPhaseLoan.loan()) {
            case Loan.PersonalLoan personalLoan -> {
                return new LoanStatus.Approved(personalLoan, 0.02);
            }
            case Loan.AutoLoan autoLoan -> {
                return new LoanStatus.Approved(autoLoan, 0.03);
            }
            case Loan.PropertyLoan propertyLoan -> {
                return new LoanStatus.Approved(propertyLoan, 0.04);
            }
        }
    }

    private LoanStatus handleAutoLoanReview(Loan.AutoLoan autoLoan) {
        return switch (autoLoan.vehicle()) {
            case Vehicle.Car car -> {
                if (autoLoan.application().applicant().creditScore() < 750) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's credit score is too low");
                }
                if (autoLoan.application().applicant().income() < 100000) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's income is too low");
                }
                if (car.make().equals("Toyota")) {
                    yield new LoanStatus.Rejected(autoLoan, "Toyota vehicles are not eligible for auto loans");
                }
                yield new LoanStatus.offerPhase(autoLoan);
            }

            case Vehicle.Motorcycle motorcycle -> {
                if (autoLoan.application().applicant().creditScore() < 600) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's credit score is too low");
                }
                if (autoLoan.application().applicant().income() < 50000) {
                    yield new LoanStatus.Rejected(autoLoan, "Applicant's income is too low");
                }
                yield new LoanStatus.offerPhase(autoLoan);
            }
        };
    }


    private LoanStatus handelPersonalLoanReview(Loan.PersonalLoan personalLoan) {
        return new LoanStatus.Approved(personalLoan, 0.02);
    }
}
