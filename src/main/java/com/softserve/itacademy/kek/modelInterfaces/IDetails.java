package com.softserve.itacademy.kek.modelInterfaces;

/**
 * Interface for exchange data with details in service layer
 */
public interface IDetails {

    String getPayload();

    void setPayload(String payload);

   String getImageUrl();

   void setImageUrl(String imageUrl);
}
