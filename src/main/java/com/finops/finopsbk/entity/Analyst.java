package com.finops.finopsbk.entity;



import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name = "analysts")
public class Analyst {
    @Id
    @Column( name = "analyst_id", nullable = false, updatable = false)
    private UUID analystId;
    private String name;
    @Column(name = "email", nullable = false,unique = true)
    private String email;
    @Column( name = "phone_number")
    private String phoneNumber;
    @Column( name = "password_hash",nullable = false)
    private String passwordHash;
    private String status;
    @Column( name = "created_at")
    private LocalDateTime createdAt;

    public Analyst() {
        this.analystId = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public UUID getAnalystId(){
        return analystId;
    }
    public void setAnalystId(UUID analystId){
        this.analystId = analystId;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPasswordHash(){
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
}
