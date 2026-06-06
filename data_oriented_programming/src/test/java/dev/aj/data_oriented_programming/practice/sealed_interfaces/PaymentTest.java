package dev.aj.data_oriented_programming.practice.sealed_interfaces;

import dev.aj.data_oriented_programming.practice.sealed_interfaces.Payment.*;
import dev.aj.data_oriented_programming.practice.sealed_interfaces.PaymentProcessor.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
class PaymentTest {

    @Test
    void paymentProcessorOOP() {

        PaymentProcessor creditCard = new PaymentProcessor.CreditCard("3238929389493284", BigDecimal.TWO);
        PaymentProcessor debitCard = new PaymentProcessor.DebitCard("3238929389493284", BigDecimal.TWO);
        PaymentProcessor cash = new PaymentProcessor.Cash(BigDecimal.TWO);
        PaymentProcessor payPal = new PaymentProcessor.PayPal("paypalId", BigDecimal.TWO);

        Stream.of(creditCard, debitCard, cash, payPal)
                .map(this::getTotalAmountCharged)
                .forEach(amount -> log.info("OOP - Total amount charged: {}", amount));
    }

    public BigDecimal getTotalAmountCharged(PaymentProcessor payment) {
        return payment.processPayment();
    }

    @Test
    void paymentDOP() {
        Payment creditCard = new Payment.CreditCard(new CardDetails("2389234789928374", "AJ B", "11/29", "234"), BigDecimal.TWO);
        Payment debitCard = new Payment.DebitCard(new CardDetails("2398742398749832", "AJ C", "01/32", "548"), BigDecimal.TWO);
        Payment cash = new Payment.Cash(BigDecimal.TWO);
        Payment payPal = new Payment.PayPal(new PayPalDetails(UUID.randomUUID().toString(), "abg@gmail.com", "AJ", "B"), BigDecimal.TWO);

        Stream.of(creditCard, debitCard, cash, payPal)
                .map(this::paymentProcessor)
                .forEach(amount -> log.info("DOP - Total amount charged: {}", amount));
    }


    public BigDecimal paymentProcessor(Payment payment) {
        log.info("Processing payment for {}", payment);
        return switch (payment) {
            case Payment.CreditCard (_, BigDecimal amount) -> {
                double riskMultiplier = 1.1;
                log.info("Need to charge {} percentage points to compensate for the scam risks with CCs", riskMultiplier);
                yield amount.multiply(BigDecimal.valueOf(riskMultiplier));
            }
            case Payment.DebitCard d -> d.amount().multiply(BigDecimal.valueOf(1.05));
            case Payment.Cash c ->  c.amount();
            case Payment.PayPal p -> p.amount();
        };
    }
}