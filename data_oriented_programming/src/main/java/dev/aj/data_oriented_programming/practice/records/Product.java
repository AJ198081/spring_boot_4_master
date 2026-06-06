import java.util.logging.Logger;

//public record Product(String name, double price) {
//}

public record Product(String name, Price price) {
}

/*record Price(BigDecimal amount, String currency){

    public boolean isGreaterThan(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must be the same");
        }

        return this.amount.compareTo(other.amount) > 0;
    }
    public boolean isEqualTo(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must be the same");
        }
        return this.amount.compareTo(other.amount) == 0;
    }

}*/

/*record Price(NonNegativeBigDecimal amount, String currency){

    public boolean isGreaterThan(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must be the same");
        }

        return this.amount.nonNegativeValue.compareTo(other.amount.nonNegativeValue) > 0;
    }
    public boolean isEqualTo(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must be the same");
        }
        return this.amount.nonNegativeValue.compareTo(other.amount.nonNegativeValue) == 0;
    }

}*/

record Price(NonNegativeBigDecimal amount, Currency currency) {

    public Price(NonNegativeBigDecimal amount, String currency) {

        if (currency == null || currency.trim().isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null");
        }

        Currency currencyEnum = Currency.valueOf(currency);
        this(amount, currencyEnum);
    }

    public Price {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
    }

    public boolean isGreaterThan(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must be the same");
        }

        return this.amount.nonNegativeValue.compareTo(other.amount.nonNegativeValue) > 0;
    }

    public boolean isEqualTo(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must be the same");
        }
        return this.amount.nonNegativeValue.compareTo(other.amount.nonNegativeValue) == 0;
    }
}

enum Currency {
    USD, INR, AUD, PKR
}

record NonNegativeBigDecimal(BigDecimal nonNegativeValue) {
    public NonNegativeBigDecimal {
        if (nonNegativeValue == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (nonNegativeValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
    }
}

static void main() {
    Logger logger = Logger.getLogger(Product.class.getName());

    Product product = new Product("Apple", new Price(new NonNegativeBigDecimal(BigDecimal.valueOf(100)), "USD"));
    Product product2 = new Product("Apple", new Price(new NonNegativeBigDecimal(BigDecimal.valueOf(10)), "USD"));
    Product product3 = new Product("Apple", new Price(new NonNegativeBigDecimal(BigDecimal.valueOf(100)), "INR"));

    BigDecimal productPrice = product.price().amount().nonNegativeValue();
    Currency currency = product.price().currency();

    if (currency == product2.price().currency()) {
        System.out.println("Currencies are the same");
    }

    Stream.ofNullable(product.getClass().getRecordComponents())
            .flatMap(Arrays::stream)
//                .map(RecordComponent::getName)
//                .map(RecordComponent::getType)
            .forEach(recordComponent -> {
                try {
                    logger.info("%s-%S".formatted(recordComponent.getName(), recordComponent.getAccessor().invoke(product)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
}