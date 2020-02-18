package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TenantPropertiesListDto {

    @Valid
    private List<TenantPropertiesDto> propertiesList;

    public TenantPropertiesListDto() {
        this(new LinkedList<>());
    }

    public TenantPropertiesListDto(@Valid List<TenantPropertiesDto> propertiesList) {
        this.propertiesList = propertiesList;
    }

    public TenantPropertiesListDto addProperties(TenantPropertiesDto tenantPropertiesDto) {
        propertiesList.add(tenantPropertiesDto);

        return this;
    }

    public List<TenantPropertiesDto> getPropertiesList() {
        return propertiesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantPropertiesListDto that = (TenantPropertiesListDto) o;
        return Objects.equals(propertiesList, that.propertiesList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertiesList);
    }

    @Override
    public String toString() {
        return "TenantPropertiesListDto{" +
                "propertiesList=" + propertiesList +
                '}';
    }
}
