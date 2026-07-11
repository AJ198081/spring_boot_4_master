package dev.aj.bank_customer.controllers;

import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import dev.aj.bank_customer.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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



        boolean asyncProcessing = Boolean.parseBoolean(request.getHeader(environment.getRequiredProperty("async.request.processing.header")));
        if (asyncProcessing) {
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


}
