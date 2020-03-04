package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddressListDto {
    @Valid
    private List<AddressDto> addressList;

    public AddressListDto() {
        this(new LinkedList<>());
    }


    public AddressListDto(@Valid List<AddressDto> addressList) {
        this.addressList = addressList;
    }

    public AddressListDto addAddress(AddressDto address) {
        addressList.add(address);

        return this;
    }

    public List<AddressDto> getAddressList() {
        return addressList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressListDto)) return false;
        AddressListDto that = (AddressListDto) o;
        return Objects.equals(addressList, that.addressList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressList);
    }

    @Override
    public String toString() {
        return "AddressListDto{"
                + "addressList="
                + addressList.stream().map(AddressDto::toString)
                .collect(Collectors.joining(","))
                + '}';
    }
}
