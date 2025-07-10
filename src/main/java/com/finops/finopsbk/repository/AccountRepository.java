package com.finops.finopsbk.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
  
import com.finops.finopsbk.entity.Account;

import jakarta.transaction.Transactional;
@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account,UUID>{
    List<Account>findByAccountId(UUID accountId);
    Optional<Account>findByAccountNumber(String accountNumber); 
    List<Account>findByKycStatus(String kycStatus);
    List<Account>findByRiskLabel(String riskLabel);

    @Modifying
    @Query("UPDATE Account a SET a.kycStatus = :kycStatus WHERE a.accountId = :id")
    void updateKycStatus(UUID id,String status);

    @Modifying
    @Query("UPDATE Account a SET a.riskScore = :score, a.riskLabel = :label WHERE a.accountId = :id")
    void updateRisk(UUID id,Float score,String label);

    @Modifying
    @Query("UPDATE Account a SET a.totalTxns = a.totalTxns + 1 WHERE a.accountId = :id")
    void incrementTotalTxns(UUID id);

    @Modifying
    @Query("UPDATE Account a SET a.fraudTxns = a.fraudTxns + 1 WHERE a.accountId = :id")
    void incrementFraudTxns(UUID id);

}
