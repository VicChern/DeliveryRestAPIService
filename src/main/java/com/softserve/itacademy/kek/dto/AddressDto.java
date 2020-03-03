package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IAddress;

public class AddressDto implements IAddress {
    private UUID guid;

    @NotNull
    @Size(max = 256)
    private String alias;

    @NotEmpty
    @Size(max = 512)
    private String address;

    @Size(max = 1024)
    private String notes;

    public AddressDto() {
    }

    public AddressDto(UUID guid, String alias, String address, String notes) {
        this.guid = guid;
        this.alias = alias;
        this.address = address;
        this.notes = notes;
    }

    public UUID getGuid() {
        return guid;
    }

    public String getAlias() {
        return alias;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDto)) return false;
        AddressDto that = (AddressDto) o;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(address, that.address) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, alias, address, notes);
    }

    @Override
    public String toString() {
        return "AddressDto{" +
                "guid='" + guid + '\'' +
                ", alias='" + alias + '\'' +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
