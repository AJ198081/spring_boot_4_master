package dev.aj.accounts.customer.services.impl;

import dev.aj.accounts.common.domain.dtos.AddressRequest;
import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.dtos.CustomerResponse;
import dev.aj.accounts.common.domain.dtos.mappers.AddressMapper;
import dev.aj.accounts.common.domain.dtos.mappers.CustomerMapper;
import dev.aj.accounts.common.domain.entities.Address;
import dev.aj.accounts.common.domain.entities.Customer;
import dev.aj.accounts.common.exceptions.CustomerAlreadyExistsException;
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
    private final AddressMapper addressMapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerData) throws CustomerAlreadyExistsException {

        if (customersRepository.existsCustomerByEmail(customerData.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email %s already exist.".formatted(customerData.getEmail()));
        }

        Customer newCustomer = customerMapper.toEntity(customerData);

        if (newCustomer.getAddresses() != null) {
            newCustomer.getAddresses()
                    .forEach(address -> address.setCustomer(newCustomer));
        }


        return customerMapper.toResponse(
                customersRepository.save(newCustomer)
        );
    }

    @Override
    public CustomerResponse getCustomer(UUID customerId) throws CustomerNotFoundException {
        return customersRepository.findCustomerByCustomerId(customerId)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Id %s doesn't exist.".formatted(customerId)));
    }

    @Override
    public void deleteCustomer(UUID customerId) {

        customersRepository.deleteCustomerByCustomerId(customerId);
    }

    @Override
    public void updateCustomer(UUID customerId, CustomerRequest updateCustomerRequest) throws CustomerNotFoundException {

        customersRepository.findCustomerByCustomerId(customerId)
                .map(customer -> {
                    customerMapper.updateEntityFromRequest(updateCustomerRequest, customer);

                    if (updateCustomerRequest.getAddresses() != null) {

                        for (AddressRequest newAddress : updateCustomerRequest.getAddresses()) {
                            customer.getAddresses()
                                    .stream()
                                    .filter(address -> newAddress.addressType().equals(address.getAddressType()))
                                    .findFirst()
                                    .ifPresentOrElse(
                                            address -> {
                                                customer.getAddresses().remove(address);
                                                customer.getAddresses().add(addressMapper.toEntity(newAddress));
                                            },
                                            () -> {
                                                Address address = addressMapper.toEntity(newAddress);
                                                address.setCustomer(customer);
                                            });
                        }

                    }

                    return customerMapper.toResponse(customersRepository.save(customer));
                })
                .orElseThrow(() -> new CustomerNotFoundException("Customer Id %s doesn't exist.".formatted(customerId)));
    }

}
