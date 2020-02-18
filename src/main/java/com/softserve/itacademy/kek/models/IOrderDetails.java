package com.softserve.itacademy.kek.models;

public interface IOrderDetails {

    Long getIdOrder();

    IOrder getOrder();

    String getPayload();

    String getImageUrl();
}
