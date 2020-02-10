package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<User> save(User user);

    Optional<Iterable<User>> saveAll(List<User> users);

    Optional<User> update(User user);

    Optional<User> get(Long id);

    List<User> getAll();

    void deleteById(Long id);

    void deleteAll();
}
