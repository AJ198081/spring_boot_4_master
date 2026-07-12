package dev.aj.bank_customer.controllers;

import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import dev.aj.bank_customer.model.dtos.CustomerResponse;
import dev.aj.bank_customer.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("${bank_customer_uri}")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final Environment environment;

    @PostMapping(path = "/")
    public ResponseEntity<CustomerCreatedResponse> createCustomer(
            @Validated @RequestBody CustomerRequest customerRequest,
            HttpServletRequest request
    ) {

        if (isAsyncRequest(request)) {
            UUID customerExternalId = customerService.createAsync(customerRequest);

            URI customerLocationURI = URI.create(request.getRequestURI()
                    .concat(String.valueOf(customerExternalId)));

            return ResponseEntity.created(customerLocationURI).build();
        }

        CustomerCreatedResponse customerCreatedResponse = customerService.create(customerRequest);

        URI customerLocationURI = URI.create(request.getRequestURI()
                .concat(String.valueOf(customerCreatedResponse.externalId())));

        return ResponseEntity.created(customerLocationURI).body(customerCreatedResponse);
    }

    @GetMapping(path = "/{externalId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID externalId) {
        return ResponseEntity.ok(customerService.getCustomer(externalId));
    }

    @PatchMapping(path = "/{externalId}/kyc-status")
    public ResponseEntity<Void> updateKycStatus(
            @PathVariable UUID externalId,
            @RequestParam String kycStatus,
            @RequestParam short version,
            HttpServletRequest request
    ) {

        if (isAsyncRequest(request)) {
            customerService.updateKycStatusAsync(externalId, kycStatus, version);
            return ResponseEntity.accepted().build();
        }
        Short updatedKycAtVersion = customerService.updateKycStatus(externalId, kycStatus, version);
        return ResponseEntity.noContent()
                .eTag(String.valueOf(updatedKycAtVersion))
                .build();
    }


    private boolean isAsyncRequest(HttpServletRequest request) {
        return Boolean.parseBoolean(request.getHeader(environment.getRequiredProperty("async.request.processing.header")));
    }
}
