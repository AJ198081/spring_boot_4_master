package dev.aj.accounts.customer.services.impl;

import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.dtos.CustomerResponse;
import dev.aj.accounts.common.domain.dtos.mappers.CustomerMapper;
import dev.aj.accounts.common.domain.entities.Customer;
import dev.aj.accounts.common.exceptions.CustomerNotFoundException;
import dev.aj.accounts.customer.services.CustomerService;
import dev.aj.accounts.customer.services.repositories.CustomersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomersRepository customersRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerData) {
        Customer newCustomer = customerMapper.toEntity(customerData);
        return customerMapper.toResponse(
                customersRepository.save(newCustomer)
        );
    }

    @Override
    public CustomerResponse getCustomer(UUID customerId) {
        return customersRepository.findCustomerByCustomerId(customerId)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Id %s doesn't exist.".formatted(customerId)));
    }

}
