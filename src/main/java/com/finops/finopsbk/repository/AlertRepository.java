package com.finops.finopsbk.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page; // Correct import

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finops.finopsbk.entity.Alert;
import com.finops.finopsbk.entity.Analyst;
import com.finops.finopsbk.dto.AlertRequest;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface AlertRepository extends JpaRepository<Alert,UUID>{
    List<Alert>findByTransaction_TxnId(UUID txnId);
    List<Alert>findBySeverity(String severity);
    List<Alert>findByStatus(String status);

    @Modifying
    @Query("UPDATE Alert a SET a.status = :status, a.resolvedBy = :analyst WHERE a.alertId = :id")
    void resolveAlert(@Param("id") UUID id, @Param("analyst") Analyst analyst, @Param("status") String status);

    @Query(value = "SELECT EXTRACT(HOUR FROM created_at) AS hour, COUNT(*) FROM alerts GROUP BY hour ORDER BY hour", nativeQuery = true)
    List<Object[]> getAlertCountsByHour();
    
    @Query("SELECT a.severity,COUNT(a) AS triggers FROM Alert a GROUP BY a.severity ORDER BY COUNT(a) DESC")
    List<Object[]> getAlertSeverity();

    @Query("SELECT new com.finops.finopsbk.dto.AlertRequest(a.alertId, a.severity, a.status, a.reason, a.createdAt, t.amount, acc.name) " +
       "FROM Alert a " +
       "JOIN a.transaction t " +
       "JOIN t.account acc " +
       "WHERE a.status IN ('UNRESOLVED', 'PENDING') " +
       "ORDER BY a.createdAt ASC")
    Page<AlertRequest> findActiveAlerts(Pageable pageable);
    
    @Query("SELECT a.status, COUNT(a) FROM Alert a GROUP BY a.status")
    List<Object[]>countAlertsByStatus();

    List<Alert>findByResolvedBy_AnalystId(UUID analystId);
}