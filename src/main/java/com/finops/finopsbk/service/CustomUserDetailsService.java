package com.finops.finopsbk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finops.finopsbk.entity.Analyst;
import com.finops.finopsbk.entity.CustomUserDetails;
import com.finops.finopsbk.repository.AnalystRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private AnalystRepository analystRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Analyst analyst = analystRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Analyst not found with email: " + email));
        return new CustomUserDetails(analyst);
    }
}
