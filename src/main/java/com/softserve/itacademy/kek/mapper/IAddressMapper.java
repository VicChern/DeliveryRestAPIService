package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.models.IAddress;

public interface IAddressMapper {

    AddressDto fromIAddress(IAddress address);
}
