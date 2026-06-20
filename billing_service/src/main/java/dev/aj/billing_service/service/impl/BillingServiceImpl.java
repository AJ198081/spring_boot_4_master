package dev.aj.billing_service.service.impl;

import dev.aj.billing_service.service.BillingService;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.invoice.InvoiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
@Slf4j
public class BillingServiceImpl implements BillingService {

    private static final ConcurrentHashMap<InvoiceRequest, Invoice> invoices = new ConcurrentHashMap<>();

    @Override
    public Invoice createInvoice(InvoiceRequest invoiceRequest) {

        UUID invoiceId = UUID.randomUUID();

        log.info("Creating invoice with id: {}", invoiceId);

        return invoices.computeIfAbsent(invoiceRequest, invoiceReq -> switch (invoiceReq) {
            case InvoiceRequest.Paid paid ->
                    new Invoice.Paid(invoiceId, paid.orderId(), paid.customerId(), paid.transactionId(), paid.priceSummary());
            case InvoiceRequest.Unpaid unpaid ->
                    new Invoice.Unpaid(unpaid.orderId(), unpaid.customerId(), null, unpaid.priceSummary(), LocalDate.now());
            case InvoiceRequest.Failed failed ->
                    new Invoice.Unpaid(failed.orderId(), failed.customerId(), null, failed.priceSummary(), LocalDate.now());
        });
    }
}
