package com.softserve.itacademy.kek.modelInterfaces;

import com.softserve.itacademy.kek.models.OrderDetails;
import com.softserve.itacademy.kek.models.OrderEvent;
import com.softserve.itacademy.kek.models.Tenant;

import java.util.List;
import java.util.UUID;

public interface IOrder {

    Long getIdOrder();

    void setIdOrder(Long idOrder);

    Tenant getIdTenant();

    void setIdTenant(Tenant idTenant);

    UUID getGuid();

    void setGuid(UUID guid);

    String getSummary();

    void setSummary(String summary);

    OrderDetails getOrderDetails();

    void setOrderDetails(OrderDetails orderDetails);

    List<OrderEvent> getOrderEventList();

    void setOrderEventList(List<OrderEvent> orderEventList);
}
