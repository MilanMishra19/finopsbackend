package com.finops.finopsbk.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import com.finops.finopsbk.entity.Transaction;

import jakarta.transaction.Transactional;
@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction,UUID>{
    List<Transaction>findByAccount_AccountId(UUID accountId);
    List<Transaction>findByIsFlaggedTrue();
    List<Transaction>findByAccount_AccountIdAndIsFraudTrue(UUID accountId);
    List<Transaction>findByIsFraudTrue();
    List<Transaction>findByStatus(String status);
    List<Transaction>findByTimeStampBetween(LocalDateTime start,LocalDateTime end);

    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status WHERE t.txnId = :id")
    void updateStatus(UUID id,String status);

    @Modifying
    @Query("UPDATE Transaction t SET t.isFraud = true,t.isFlagged = true,t.status = 'BLOCKED' WHERE t.txnId = :id")
    void markAsFraud(UUID id);

    @Query("SELECT SUM(t.amount) FROM Transaction t")
    BigDecimal getTotalTransactionAmount();

    @Query("SELECT t.method, COUNT(t) FROM Transaction t GROUP BY t.method")
    List<Object[]> getPaymentMethodCounts();

    @Query("SELECT t.method,SUM(t.amount) FROM Transaction t GROUP BY t.method ORDER BY SUM(t.amount) DESC")
    List<Object[]>getAmountByMethod();
    
    @Query("SELECT SUM(t.amount) FROM Transaction t JOIN Alert a ON a.transaction = t WHERE a.status = 'RESOLVED'")
    BigDecimal getResolvedRevenue();

    @Query("SELECT t.location,SUM(t.amount) FROM Transaction t WHERE t.isFraud = true GROUP BY t.location")
    List<Object[]>getFraudByCity();

    @Query(value="SELECT DATE_TRUNC('hour',t.timestamp) AS hour,"+
            "SUM(CASE WHEN t.is_fraud=true THEN t.amount ELSE 0 END) as fraudLoss,"+
            "SUM(CASE WHEN t.is_fraud=false THEN t.amount ELSE 0 END) as revenue "+
            "FROM transactions t GROUP BY hour ORDER BY hour", nativeQuery = true)
    List<Object[]>getRevenueByHour();

    @Query("SELECT SUM(CASE WHEN t.isFraud=true THEN t.amount ELSE 0 END) FROM Transaction t")
    BigDecimal getRevenue();

    @Query("SELECT SUM(CASE WHEN t.isFraud=false THEN t.amount ELSE 0 END) FROM Transaction t")
    BigDecimal getFraud();

}
