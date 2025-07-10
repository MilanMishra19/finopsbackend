package com.finops.finopsbk.entity;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "alerts")
public class Alert {
    @Id
    @Column( name = "alert_id",nullable = false)
    private UUID alertId;
    @OneToOne
    @JoinColumn( name = "transaction_txn_id",referencedColumnName = "txn_id")
    private Transaction transaction;
    private String severity;
    private String reason;
    private String status;
    @ManyToOne
    @JoinColumn( name = "resolved_by", referencedColumnName = "analyst_id")
    private Analyst resolvedBy;
    @Column( name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public UUID getAlertId(){
        return alertId;
    }
    public void setAlertId(UUID alertId){
        this.alertId = alertId;
    }
    public Transaction getTransaction(){
        return transaction;
    }
    public void setTransaction(Transaction transaction){
        this.transaction = transaction;
    }
    public String getSeverity(){
        return severity;
    }
    public void setSeverity(String severity){
        this.severity = severity;
    }
    public String getReason(){
        return reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public Analyst getResolvedBy(){
        return resolvedBy;
    }
    public void setResolvedBy(Analyst resolvedBy){
        this.resolvedBy = resolvedBy;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
}
