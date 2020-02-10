package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.UserDetails;

import java.util.List;
import java.util.Optional;

public interface IUserDetailsService {

    Optional<UserDetails> save(UserDetails userDetails);

    Optional<Iterable<UserDetails>> saveAll(List<UserDetails> userDetailsList);

    Optional<UserDetails> update(UserDetails userDetails);

    Optional<UserDetails> get(Long id);

    List<UserDetails> getAll();

    void deleteById(Long id);

    void deleteAll();
}
