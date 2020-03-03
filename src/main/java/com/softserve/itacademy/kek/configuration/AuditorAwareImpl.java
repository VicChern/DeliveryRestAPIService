package com.softserve.itacademy.kek.configuration;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Implementation of AuditorAware for components that are aware of the application's current auditor
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

//       TODO: Can return currently logged in user name
//        return Optional.ofNullable(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName());

        return Optional.of("Kek");
    }
}
