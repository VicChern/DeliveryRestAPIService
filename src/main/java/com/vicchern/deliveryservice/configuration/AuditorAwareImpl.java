package com.vicchern.deliveryservice.configuration;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

/**
 * Implementation of AuditorAware for components that are aware of the application's current auditor
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        return Optional.of("DeliveryService");
    }
}
