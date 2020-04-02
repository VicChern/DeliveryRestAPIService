package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.models.IAddress;
import com.softserve.itacademy.kek.models.impl.Address;

/**
 * Interface for {@link Address} mapping
 */
public interface IAddressMapper {

    /**
     * Transform {@link IAddress} to {@link AddressDto}
     *
     * @param address
     * @return
     */
    AddressDto fromIAddress(IAddress address);
}
