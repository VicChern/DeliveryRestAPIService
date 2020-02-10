package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.UserDetails;
import com.softserve.itacademy.kek.repositories.UserDetailsRepository;
import com.softserve.itacademy.kek.services.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements IUserDetailsService {

    private UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public Optional<UserDetails> save(UserDetails userDetails) {
        return Optional.of(userDetailsRepository.save(userDetails));
    }

    @Override
    public Optional<Iterable<UserDetails>> saveAll(List<UserDetails> userDetailsList) {
        return Optional.of(userDetailsRepository.saveAll(userDetailsList));
    }

    @Override
    public Optional<UserDetails> update(UserDetails userDetails) {
        return Optional.of(userDetailsRepository.save(userDetails));
    }

    @Override
    public Optional<UserDetails> get(Long id) {
        return userDetailsRepository.findById(id);
    }

    @Override
    public List<UserDetails> getAll() {
        return (List<UserDetails>) userDetailsRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userDetailsRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userDetailsRepository.deleteAll();
    }

}
