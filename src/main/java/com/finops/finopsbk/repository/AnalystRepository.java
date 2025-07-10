package com.finops.finopsbk.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finops.finopsbk.entity.Analyst;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface AnalystRepository extends JpaRepository<Analyst, UUID> {
    Optional<Analyst>findByEmail(String email);
    Optional<Analyst>findByName(String name);
    List<Analyst>findByStatus(String status);
}
