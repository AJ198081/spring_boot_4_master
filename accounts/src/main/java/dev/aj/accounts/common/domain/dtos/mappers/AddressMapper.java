package dev.aj.accounts.common.domain.dtos.mappers;

import dev.aj.accounts.common.domain.dtos.AddressRequest;
import dev.aj.accounts.common.domain.dtos.AddressResponse;
import dev.aj.accounts.common.domain.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@SuppressWarnings("unused")
public interface AddressMapper {

    @Mapping(target = "metaData", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Address toEntity(AddressRequest addressRequest);

    AddressResponse toResponse(Address address);

}
