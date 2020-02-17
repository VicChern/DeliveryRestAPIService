package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class AddressDto {

    @NotNull
    private String guid;

    @NotNull
    @Size(max = 256)
    private String alias;

    @NotNull
    @Size(max = 512)
    private String address;

    @Size(max = 1024)
    private String notes;

    public AddressDto() {
    }

    public AddressDto(String guid, String alias, String address, String notes) {
        this.guid = guid;
        this.alias = alias;
        this.address = address;
        this.notes = notes;
    }

    public String getGuid() {
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
