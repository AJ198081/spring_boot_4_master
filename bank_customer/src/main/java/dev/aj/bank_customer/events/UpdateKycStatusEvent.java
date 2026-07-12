package dev.aj.bank_customer.events;

import java.util.UUID;

public record UpdateKycStatusEvent(UUID externalId, String kycStatus, short fromVersion) {
}
