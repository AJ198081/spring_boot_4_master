package dev.aj.accounts.common.domain.dtos.mappers;

import dev.aj.accounts.common.domain.dtos.AddressRequest;
import dev.aj.accounts.common.domain.dtos.AddressResponse;
import dev.aj.accounts.common.domain.entities.Address;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AddressMapper {

    Address toEntity(AddressRequest addressRequest);

    AddressResponse toResponse(Address address);

}
