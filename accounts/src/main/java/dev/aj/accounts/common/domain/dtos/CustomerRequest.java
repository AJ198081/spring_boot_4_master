package dev.aj.accounts.common.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request object to create a new Customer")
public final class CustomerRequest {
    @Schema(description = "The customer's first name", example = "John")
    private String firstName;
    @Schema(description = "The customer's last name", example = "Doe")
    private String lastName;
    @Schema(description = "The customer's email address", example = "abg@gmail.com")
    private String email;
    @Schema(description = "The customer's phone number", example = "0438412695")
    private String phoneNumber;
    @Schema(description = "The customer's addresses")
    private Set<AddressRequest> addresses;
}
