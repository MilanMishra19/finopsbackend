package com.finops.finopsbk.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column( name = "account_id",nullable = false)
    private UUID accountId;
    @Column( name = "account_number")
    private String accountNumber;
    private String name;
    private LocalDate dob;
    private String city;
    @Column( name = "kyc_status")
    private String kycStatus;
    @Column( name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column( name = "risk_score")
    private Float riskScore = 0.0f;
    @Column( name = "risk_label")
    private String riskLabel = "low";
    @Column( name = "total_txns")
    private Integer totalTxns = 0;
    @Column( name = "fraud_txns")
    private Integer fraudTxns = 0;

    public UUID getAccountId(){
        return accountId;
    }
    public void setAccountId(UUID accountId){
        this.accountId = accountId;
    }
    public String getAccountNumber(){
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public LocalDate getDob(){
        return dob;
    }
    public void setDob(LocalDate dob){
        this.dob = dob;
    }
    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getKycStatus(){
        return kycStatus;
    }
    public void setKycStatus(String kycStatus){
        this.kycStatus = kycStatus;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public Float getRiskScore(){
        return riskScore;
    }
    public void setRiskScore(Float riskScore){
        this.riskScore = riskScore;
    }
    public String getRiskLabel(){
        return riskLabel;
    }
    public void setRiskLabel(String riskLabel){
        this.riskLabel = riskLabel;
    }
    public Integer getTotalTxns(){
        return totalTxns;
    }
    public void setTotalTxns(Integer totalTxns){
        this.totalTxns = totalTxns;
    }
    public Integer getFraudTxns(){
        return fraudTxns;
    }
    public void setFraudTxns(Integer fraudTxns){
        this.fraudTxns = fraudTxns;
    }
}

