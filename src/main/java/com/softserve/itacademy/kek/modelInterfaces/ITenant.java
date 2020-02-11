package com.softserve.itacademy.kek.modelInterfaces;

import com.softserve.itacademy.kek.models.Address;
import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.models.TenantProperties;
import com.softserve.itacademy.kek.models.User;

import java.util.List;
import java.util.UUID;

public interface ITenant {

    Long getIdTenant();

    void setIdTenant(Long idTenant);

    UUID getGuid();

    void setGuid(UUID guid);

    String getName();

    void setName(String name);

    User getTenantOwner();

    void setTenantOwner(User tenantOwner);

    TenantDetails getTenantDetails();

    void setTenantDetails(TenantDetails tenantDetails);

    List<TenantProperties> getTenantPropertiesList();

    void setTenantPropertiesList(List<TenantProperties> tenantPropertiesList);

    List<Address> getAddressList();

    void setAddressList(List<Address> addressList);
}
