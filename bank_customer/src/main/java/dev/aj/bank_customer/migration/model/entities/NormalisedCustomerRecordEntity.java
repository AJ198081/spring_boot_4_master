package dev.aj.bank_customer.migration.model.entities;

import dev.aj.bank_customer.model.entities.KycStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;


@Table(name = "normalised_customers")
public record NormalisedCustomerRecordEntity(
        @Id
        Long id,
        UUID externalId,
        short version,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String email,
        String phone,
        KycStatus kycStatus,
        String requestFingerPrint,
        boolean active,

        @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
        AuditMetaDataRecord auditMetaData,

        Set<NormalisedAddressRecordEntity> addresses) {

    public NormalisedCustomerRecordEntity withAddresses(Set<NormalisedAddressRecordEntity> updatedAddress) {
        return new NormalisedCustomerRecordEntity(id, externalId, version, firstName, lastName, dateOfBirth, email, phone, kycStatus, requestFingerPrint, active, auditMetaData, updatedAddress);
    }
}
