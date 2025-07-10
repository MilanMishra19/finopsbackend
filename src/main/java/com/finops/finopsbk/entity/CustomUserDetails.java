package com.finops.finopsbk.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    private final Analyst analyst;
    public CustomUserDetails(Analyst analyst) {
        this.analyst = analyst;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ANALYST"));
    }
    @Override
    public String getPassword() {
        return analyst.getPasswordHash();
    }
    @Override
    public String getUsername() {
        return analyst.getEmail();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(analyst.getStatus());
    }
    public Analyst getAnalyst () {
        return analyst;
    }

}
