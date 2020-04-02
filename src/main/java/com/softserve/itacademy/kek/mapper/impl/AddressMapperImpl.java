package com.softserve.itacademy.kek.mapper.impl;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.mapper.IAddressMapper;
import com.softserve.itacademy.kek.models.IAddress;

public class AddressMapperImpl implements IAddressMapper {
    @Override
    public AddressDto fromIAddress(IAddress address) {
        return new AddressDto(address.getGuid(), address.getAlias(), address.getAddress(), address.getNotes());
    }
}
