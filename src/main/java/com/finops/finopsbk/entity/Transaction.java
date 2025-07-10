package com.finops.finopsbk.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column( name = "txn_id",nullable = false)
    private UUID txnId;
    @ManyToOne
    @JoinColumn( name = "account_id",nullable = false,referencedColumnName = "account_id")
    private Account account;
    private BigDecimal amount;
    private String method;
    @Column( name = "timestamp")
    private LocalDateTime timeStamp;
    private String location;
    @Column( name = "device_id")
    private String deviceId;
    private String status = "SUCCESS";
    private Boolean isFraud = false;
    private Boolean isFlagged = false;

    public UUID getTxnId(){
        return txnId;
    }
    public void setTxnId(UUID txnId){
        this.txnId = txnId;
    }
    public Account getAccount(){
        return account;
    }
    public void setAccount(Account account){
        this.account = account;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public String getMethod(){
        return method;
    }
    public void setMethod(String method){
        this.method = method;
    }
    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }
    public void setTimeStamp(LocalDateTime timeStamp){
        this.timeStamp = timeStamp;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getDeviceId(){
        return deviceId;
    }
    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public Boolean getIsFraud(){
        return isFraud;
    }
    public void setIsFraud(Boolean isFraud){
        this.isFraud = isFraud;
    }
    public Boolean getIsFlagged(){
        return isFlagged;
    }
    public void setIsFlagged(Boolean isFlagged){
        this.isFlagged = isFlagged;
    }
};
