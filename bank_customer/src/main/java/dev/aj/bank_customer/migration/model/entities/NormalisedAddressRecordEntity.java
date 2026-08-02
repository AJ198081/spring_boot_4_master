package dev.aj.bank_customer.migration.model.entities;

import dev.aj.bank_customer.model.entities.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "normalised_addresses")
public record NormalisedAddressRecordEntity(
        @Id
        String id,

        Address address,

        @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
        AuditMetaDataRecord auditMetaDataRecord) {

    public NormalisedAddressRecordEntity withId(String identifier) {
        return new NormalisedAddressRecordEntity(identifier, address, auditMetaDataRecord);
    }
}
