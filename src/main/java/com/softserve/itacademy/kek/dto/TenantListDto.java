package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TenantListDto {
    @Valid
    @NotNull
    private List<TenantDto> tenantList;

    public TenantListDto() {
        this(new LinkedList<>());
    }

    public TenantListDto(@Valid @NotNull List<TenantDto> tenantList) {
        this.tenantList = tenantList;
    }

    public List<TenantDto> getTenantList() {
        return tenantList;
    }

    public TenantListDto addTenant(TenantDto tenant) {
        tenantList.add(tenant);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantListDto that = (TenantListDto) o;
        return Objects.equals(tenantList, that.tenantList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantList);
    }

    @Override
    public String toString() {
        return "TenantListDto{" +
                "tenantList=" + tenantList.stream().map(TenantDto::toString).collect(Collectors.joining(",")) +
                '}';
    }
}

