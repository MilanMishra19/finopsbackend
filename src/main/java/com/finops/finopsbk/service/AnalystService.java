package com.finops.finopsbk.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finops.finopsbk.entity.Analyst;
import com.finops.finopsbk.repository.AnalystRepository;
import com.finops.finopsbk.dto.RegisterRequest; 

@Service
public class AnalystService {
    @Autowired
    private final AnalystRepository analystRepository;
    private final PasswordEncoder passwordEncoder;

    public AnalystService(AnalystRepository analystRepository, PasswordEncoder passwordEncoder) {
        this.analystRepository = analystRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Analyst createAnalyst(RegisterRequest registerRequest) { // Use the DTO here
        // 1. Create a new Analyst entity and set properties from the DTO
        Analyst analyst = new Analyst();
        // The Analyst constructor already sets analystId and createdAt, so these are redundant.
        analyst.setName(registerRequest.getName());
        analyst.setPhoneNumber(registerRequest.getPhoneNumber());
        analyst.setEmail(registerRequest.getEmail());
        analyst.setStatus("ACTIVE"); 
        analyst.setCreatedAt(LocalDateTime.now());
        
        // 2. HASH THE PASSWORD from the DTO before setting it on the entity
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        analyst.setPasswordHash(hashedPassword);

        // 3. Save the entity to the database
        return analystRepository.save(analyst);
    }
    public List<Analyst> getAllAnalysts() {
        return analystRepository.findAll();
    }

    public Optional<Analyst> getAnalystById(UUID analystId) {
        return analystRepository.findById(analystId);
    }

    public Optional<Analyst> getAnalystByEmail(String email) {
        return analystRepository.findByEmail(email);
    }
}