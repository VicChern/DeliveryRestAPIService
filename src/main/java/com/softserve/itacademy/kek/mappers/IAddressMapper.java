package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.models.IAddress;
import com.softserve.itacademy.kek.models.impl.Address;

/**
 * Interface for {@link Address} mapping
 */
@Mapper
public interface IAddressMapper {

    IAddressMapper INSTANCE = Mappers.getMapper(IAddressMapper.class);

    /**
     * Transform {@link IAddress} to {@link AddressDto}
     *
     * @param address
     * @return addressDto
     */
    AddressDto toAddressDto(IAddress address);
}
