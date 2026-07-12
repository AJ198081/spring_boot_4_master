package dev.aj.bank_customer.services.impl;

import dev.aj.bank_commons.utils.FingerPrint;
import dev.aj.bank_customer.events.CustomerCreateEvent;
import dev.aj.bank_customer.events.UpdateKycStatusEvent;
import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import dev.aj.bank_customer.model.entities.Customer;
import dev.aj.bank_customer.model.entities.KycStatus;
import dev.aj.bank_customer.model.mappers.CustomerMapper;
import dev.aj.bank_customer.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements dev.aj.bank_customer.services.CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TransactionTemplate transactionTemplate;

    @Override
    public CustomerCreatedResponse create(CustomerRequest customerRequest) {

        log.debug("Creating a new customer with a request: {}", customerRequest);
//  What can go wrong? What if someone else with the exact same customer details sends the request? They will now have customer's details.
        Customer customer = customerRepository.findByRequestFingerPrint(FingerPrint.generateFor(customerRequest))
                .orElse(registerNewCustomer(customerRequest));

        return customerMapper.toCreatedResponse(customer);
    }

    @Override
    public Customer create(CustomerCreateEvent customerCreateEvent) {

        return customerRepository.findByRequestFingerPrint(FingerPrint.generateFor(customerCreateEvent.customerRequest()))
                .orElseGet(() -> {
                            Customer newCustomer = buildNewCustomer(customerCreateEvent.customerRequest());
                            newCustomer.setExternalId(customerCreateEvent.withExternalId());
                            return customerRepository.save(newCustomer);
                        }
                );
    }

    @Override
    public UUID createAsync(CustomerRequest customerRequest) {

        UUID externalId = UUID.randomUUID();

        transactionTemplate.executeWithoutResult(status -> {
            applicationEventPublisher.publishEvent(new CustomerCreateEvent(customerRequest, externalId));
            status.isCompleted();
        });


        return externalId;
    }

    @Override
    public Short updateKycStatus(UUID customerId, String kycStatus) {

        return transactionTemplate.execute(_ ->
                customerRepository.findByExternalId(customerId)
                        .map(customer -> {
                            KycStatus parsedKycStatus = KycStatus.valueOf(kycStatus);
                            customer.setKycStatus(parsedKycStatus);
                            customer.setActive(parsedKycStatus.equals(KycStatus.APPROVED));

                            return customerRepository.saveAndFlush(customer).getVersion();
                        })
                        .orElseThrow(() -> new IllegalArgumentException("Customer with externalId: %s not found."
                                .formatted(customerId)))
        );
    }

    @Override
    @Transactional
    public void updateKycStatusAsync(UUID externalId, String kycStatus) {
        applicationEventPublisher.publishEvent(new UpdateKycStatusEvent(externalId, kycStatus));
    }

    private @NonNull Customer registerNewCustomer(CustomerRequest customerRequest) {
        Customer newCustomerRequest = buildNewCustomer(customerRequest);

        return customerRepository.save(newCustomerRequest);
    }

    private @NonNull Customer buildNewCustomer(CustomerRequest customerRequest) {
        Customer newCustomerRequest = customerMapper.toEntity(customerRequest);
        newCustomerRequest.setActive(false);
        newCustomerRequest.setKycStatus(KycStatus.PENDING);
        newCustomerRequest.setRequestFingerPrint(FingerPrint.generateFor(customerRequest));
        newCustomerRequest.setExternalId(UUID.randomUUID());
        return newCustomerRequest;
    }
}
