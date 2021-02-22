package com.vicchern.deliveryservice.exception;

import com.vicchern.deliveryservice.services.impl.CloudStorageServiceImpl;

/**
 * Exception for {@link  CloudStorageServiceImpl}
 */
public class CloudStorageServiceException extends DeliveryServiceException {

    public CloudStorageServiceException(String message) {
        super(message);
    }

    public CloudStorageServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudStorageServiceException(Throwable cause) {
        super(cause);
    }
}