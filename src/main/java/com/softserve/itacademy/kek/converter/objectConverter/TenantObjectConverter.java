package com.softserve.itacademy.kek.converter.objectConverter;

import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.objects.DetailsObject;
import com.softserve.itacademy.kek.objects.TenantObject;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TenantObjectConverter {

    public Tenant transform(TenantObject tenantObject, User user) {
        Tenant tenant = new Tenant();
        tenant.setGuid(tenantObject.getGuid() == null ? UUID.randomUUID(): tenantObject.getGuid());
        tenant.setName(tenantObject.getName());
        tenant.setTenantOwner(user);

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setImageUrl(tenantObject.getTenantDetailsObject().getImageUrl());
        tenantDetails.setPayload(tenantObject.getTenantDetailsObject().getPayload());
        tenantDetails.setTenant(tenant);

        tenant.setTenantDetails(tenantDetails);

        return tenant;
    }

    public TenantObject transform(Tenant tenant) {
        DetailsObject detailsObject = new DetailsObject(
                tenant.getTenantDetails().getPayload(),
                tenant.getTenantDetails().getImageUrl()
        );

        TenantObject tenantObject = new TenantObject(
                tenant.getGuid(),
                tenant.getTenantOwner().getGuid().toString(),
                tenant.getName(),
                detailsObject
        );

        return tenantObject;
    }
}
