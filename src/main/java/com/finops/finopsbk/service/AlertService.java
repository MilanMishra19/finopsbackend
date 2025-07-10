package com.finops.finopsbk.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finops.finopsbk.dto.AlertRequest;
import com.finops.finopsbk.entity.Alert;
import com.finops.finopsbk.entity.Analyst;
import com.finops.finopsbk.entity.Transaction;
import com.finops.finopsbk.repository.AlertRepository;
import com.finops.finopsbk.repository.AnalystRepository;
import com.finops.finopsbk.repository.TransactionRepository;

import jakarta.transaction.Transactional;


@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AnalystRepository analystRepository;
    
    public List<Alert> getAllAlerts() {
    return alertRepository.findAll();
    }
    public Alert createAlert(Alert alert){
        alert.setAlertId(UUID.randomUUID());
        alert.setCreatedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    public Optional<Alert>getAlertById(UUID id){
        return alertRepository.findById(id);
    }

    public List<Alert>getAlertByTxnId(UUID txn){
        return alertRepository.findByTransaction_TxnId(txn);
    }

    public List<Alert>getAlertBySeverity(String severity){
        return alertRepository.findBySeverity(severity);
    }

    public List<Alert>getAlertByStatus(String status){
        return alertRepository.findByStatus(status);
    }

    public List<Alert>getAllUnresolved(){
        return alertRepository.findByStatus("PENDING");
    }

    @Transactional
    public void resolveAlert(UUID alertId, String status, String analystId) {
    // 1. Find the alert
    Alert alert = alertRepository.findById(alertId)
        .orElseThrow(() -> new RuntimeException("Alert not found with ID: " + alertId));

    Analyst analyst = analystRepository.findById(UUID.fromString(analystId))
    .orElseThrow(() -> new RuntimeException("Analyst not found"));

    // 3. Update the alert using the JPQL method
    alertRepository.resolveAlert(alertId, analyst, status);

    // 4. Additional logic if RESOLVED
    if ("RESOLVED".equalsIgnoreCase(status)) {
        UUID transactionId = alert.getTransaction().getTxnId();

        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);

        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();

            if (Boolean.TRUE.equals(transaction.getIsFraud())) {
                transaction.setIsFraud(false);
                transaction.setIsFlagged(false);
                transactionRepository.save(transaction);

                UUID accountId = transaction.getAccount().getAccountId();
                accountService.decrementFraudTxns(accountId);
            }
        }
    }
}


   public List<Map<String, Object>> getAlertsByHour() {
    List<Object[]> results = alertRepository.getAlertCountsByHour();
    List<Map<String, Object>> hourlyData = new ArrayList<>();

    for (Object[] row : results) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("hour", ((Number) row[0]).intValue());  
        entry.put("count", ((Number) row[1]).longValue()); 
        hourlyData.add(entry);
    }

    return hourlyData;
    }
    
    public List<Map<String,Object>> countStatusGrouped(){
        List<Object[]> results = alertRepository.countAlertsByStatus();
        List<Map<String,Object>> data = new ArrayList<>();
        for(Object[] row : results) {
            Map<String,Object> entry = new HashMap<>();
            entry.put("status",row[0]);
            entry.put("count",((Number)row[1]).longValue());
            data.add(entry);
        }
        return data;
    }
    public List<Map<String,Object>> getAlertsSeverity(){
        List<Object[]> results = alertRepository.getAlertSeverity();
        List<Map<String,Object>> data = new ArrayList<>();
        for(Object[] row:results){
            Map<String,Object> entry = new HashMap<>();
            entry.put("severity",row[0]);
            entry.put("triggers",((Number)row[1]).longValue());
            data.add(entry);
        }
        return data;
    }

    public org.springframework.data.domain.Page<AlertRequest> getActiveAlerts(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return alertRepository.findActiveAlerts(pageable);
    }

    public List<AlertRequest> getResolvedAlertRequests(UUID analystId){
        List<Alert> resolvedAlerts = alertRepository.findByResolvedBy_AnalystId(analystId);
        return resolvedAlerts.stream()
                .map(alert -> new AlertRequest(
                        alert.getAlertId(),
                        alert.getSeverity(),
                        alert.getStatus(),
                        alert.getReason(),
                        alert.getCreatedAt(),
                        alert.getTransaction().getAmount(),
                        alert.getTransaction().getAccount().getName()))
                .toList();
    }
}