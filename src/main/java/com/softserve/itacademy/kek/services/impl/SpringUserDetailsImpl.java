package com.softserve.itacademy.kek.services.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.softserve.itacademy.kek.services.IUserService;

public class SpringUserDetailsImpl implements UserDetails {

    @Autowired
    private IUserService userService;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        userService.getUserAuthorities();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
