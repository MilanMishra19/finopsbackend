package com.finops.finopsbk.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Change from Timestamp to LocalDateTime
import java.util.UUID;

public class AlertRequest {

    private UUID alertId;
    private String severity;
    private String status;
    private String reason;
    private LocalDateTime createdAt; 
    private BigDecimal amount;
    private String name;

    public AlertRequest(UUID alertId, String severity, String status, String reason,
                        LocalDateTime createdAt, BigDecimal amount, String name) { 
        this.alertId = alertId;
        this.severity = severity;
        this.status = status;
        this.reason = reason;
        this.createdAt = createdAt;
        this.amount = amount;
        this.name = name;
    }

    // Getters and Setters
    public UUID getAlertId() {
        return alertId;
    }

    public void setAlertId(UUID alertId) {
        this.alertId = alertId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { 
        this.name = name;
    }
}