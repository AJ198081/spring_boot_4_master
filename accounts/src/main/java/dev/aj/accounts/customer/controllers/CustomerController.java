package dev.aj.accounts.customer.controllers;

import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.dtos.CustomerResponse;
import dev.aj.accounts.common.exceptions.CustomerAlreadyExistsException;
import dev.aj.accounts.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("${api.basePath.customers}")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/")
    public ResponseEntity<HttpStatus> createCustomer(@RequestBody CustomerRequest customerRequest) throws CustomerAlreadyExistsException {

        CustomerResponse customerCreated = customerService.createCustomer(customerRequest);

        URI customerLocation = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment("{customerId}")
                .buildAndExpand(customerCreated.customerId())
                .toUri();

        return ResponseEntity.created(customerLocation).build();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID customerId){
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }


}
