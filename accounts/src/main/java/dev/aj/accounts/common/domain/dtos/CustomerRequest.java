package dev.aj.accounts.common.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class CustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Set<AddressRequest> addresses;
}
