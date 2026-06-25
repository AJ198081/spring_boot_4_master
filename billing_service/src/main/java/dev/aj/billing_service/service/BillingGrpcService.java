package dev.aj.billing_service.service;

import dev.aj.billing.BillingGrpc;
import dev.aj.billing.Invoice;
import dev.aj.billing.InvoiceRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class BillingGrpcService extends BillingGrpc.BillingImplBase {

    private final BillingService billingService;

    @Override
    public void createInvoice(InvoiceRequest request, StreamObserver<Invoice> responseObserver) {

        super.createInvoice(request, responseObserver);
    }
}
