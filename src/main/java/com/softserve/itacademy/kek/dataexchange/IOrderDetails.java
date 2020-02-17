package com.softserve.itacademy.kek.dataexchange;

public interface IOrderDetails {

    Long getIdOrder();

    IOrder getOrder();

    String getPayload();

    String getImageUrl();
}
