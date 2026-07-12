package dev.aj.bank_customer.services.listeners;

import dev.aj.bank_customer.events.CustomerCreateEvent;
import dev.aj.bank_customer.events.UpdateKycStatusEvent;
import dev.aj.bank_customer.model.entities.Customer;
import dev.aj.bank_customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListeners {

    private final CustomerService customerService;

    @ApplicationModuleListener
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {
                    DataIntegrityViolationException.class,
                    OptimisticLockingFailureException.class
            }
    )
    public void on(CustomerCreateEvent customerCreateEvent) {
        Customer newCustomerCreated = customerService.create(customerCreateEvent);

        log.info("Customer created with internal ID: {}, and external ID: {}", newCustomerCreated.getId(), newCustomerCreated.getExternalId());
    }

    @ApplicationModuleListener
    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public void on(UpdateKycStatusEvent updateKycStatusEvent) {
        Short updatedKycStatus = customerService.updateKycStatus(updateKycStatusEvent.externalId(), updateKycStatusEvent.kycStatus());
        log.info("Customer ID {}, KYC status updated at snapshot: {}", updateKycStatusEvent.externalId(), updatedKycStatus);
    }

}
