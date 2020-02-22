package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TenantPropertiesListDto {

    @Valid
    private List<TenantPropertiesDto> tenantPropertiesList;

    public TenantPropertiesListDto() {
        this(new LinkedList<>());
    }


    public TenantPropertiesListDto(@Valid List<TenantPropertiesDto> tenantDtoList) {
        this.tenantPropertiesList = tenantDtoList;
    }

    public TenantPropertiesListDto addTenantProperty(TenantPropertiesDto tenantProperty) {
        tenantPropertiesList.add(tenantProperty);

        return this;
    }

    public List<TenantPropertiesDto> getTenantPropertiesList() {
        return tenantPropertiesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantPropertiesListDto that = (TenantPropertiesListDto) o;
        return Objects.equals(tenantPropertiesList, that.tenantPropertiesList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantPropertiesList);
    }

    @Override
    public String toString() {
        return "TenantPropertiesListDto{"
                + "tenantPropertiesList="
                + tenantPropertiesList
                .stream()
                .map(TenantPropertiesDto::toString)
                .collect(Collectors.joining(","))
                + '}';
    }
}
