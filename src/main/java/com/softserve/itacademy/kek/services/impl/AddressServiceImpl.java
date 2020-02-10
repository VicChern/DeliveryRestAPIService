package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.Address;
import com.softserve.itacademy.kek.repositories.AddressRepository;
import com.softserve.itacademy.kek.services.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements IAddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Optional<Address> save(Address address) {
        return Optional.of(addressRepository.save(address));
    }

    @Override
    public Optional<Iterable<Address>> saveAll(List<Address> addressList) {
        return Optional.of(addressRepository.saveAll(addressList));
    }

    @Override
    public Optional<Address> update(Address address) {
        return Optional.of(addressRepository.save(address));
    }

    @Override
    public Optional<Address> get(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public List<Address> getAll() {
        return (List<Address>) addressRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        addressRepository.deleteAll();
    }

}
