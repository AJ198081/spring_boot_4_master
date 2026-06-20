package dev.aj.order_service.service.impl;

import dev.aj.order_service.client.BillingClient;
import dev.aj.order_service.client.PaymentClient;
import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.customer.Customer;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.invoice.InvoiceRequest;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.payment.PaymentRequest;
import dev.aj.order_service.model.payment.PaymentStatus;
import dev.aj.order_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@NullMarked
public class PaymentServiceImpl implements PaymentService {

    private final PaymentClient paymentClient;
    private final BillingClient billingClient;

    @Override
    public Invoice processPayment(Order order, PriceSummary priceSummary) {

        PaymentRequest paymentRequest = new PaymentRequest(
                order.customer().id(),
                order.orderId(),
                priceSummary.total());

        PaymentStatus processedPaymentStatus = this.paymentClient.process(paymentRequest);

        return switch (processedPaymentStatus) {
            case PaymentStatus.Completed completed -> this.generatePaidInvoice(order, priceSummary, completed);
            case PaymentStatus.Failed failed -> this.generateFailedInvoice(order, priceSummary, failed);
            case PaymentStatus.Pending pending -> this.generateUnpaidInvoice(order, pending, priceSummary);
        };
    }

    private Invoice generateUnpaidInvoice(Order order, PaymentStatus.Pending pendingPaymentStatus, PriceSummary priceSummary) {

        switch (order.customer()) {
            case Customer.WholesaleCustomer wholesaleCustomer ->
                    this.billingClient.createInvoice(new InvoiceRequest.Unpaid(pendingPaymentStatus, priceSummary));
            case Customer.RetailCustomer retailCustomer ->
                    this.billingClient.createInvoice(new InvoiceRequest.Failed(order.orderId(), order.customer().id(), priceSummary));
        }

        return this.billingClient.createInvoice(new InvoiceRequest.Unpaid(pendingPaymentStatus, priceSummary));
    }

    private Invoice generateFailedInvoice(Order order, PriceSummary priceSummary, PaymentStatus.Failed failed) {

        return this.billingClient.createInvoice(new InvoiceRequest.Failed(order.orderId(), order.customer().id(), priceSummary));
    }

    private Invoice generatePaidInvoice(Order order, PriceSummary priceSummary, PaymentStatus.Completed completed) {

        InvoiceRequest.Paid paidInvoiceRequest = new InvoiceRequest.Paid(order.orderId(), order.customer().id(), completed.transactionId(), priceSummary);
        return this.billingClient.createInvoice(paidInvoiceRequest);
    }
}
