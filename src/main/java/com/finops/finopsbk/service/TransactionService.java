package com.finops.finopsbk.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finops.finopsbk.dto.FraudPrediction;
import com.finops.finopsbk.entity.Transaction;
import com.finops.finopsbk.repository.TransactionRepository;
import com.finops.finopsbk.entity.Account;
import com.finops.finopsbk.entity.Alert;
import com.finops.finopsbk.repository.AccountRepository;
import com.finops.finopsbk.repository.AlertRepository;
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired 
    private PredictionService predictionService;

    @Autowired 
    private AccountRepository accountRepository; 

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AccountService accountService;

    public Transaction createTransaction(Transaction txn){
        UUID accountIdFromTxn = txn.getAccount().getAccountId(); 
       
        Account existingAccount = accountRepository.findById(accountIdFromTxn)
                                             .orElseThrow(() -> new RuntimeException("Account not found for ID: " + accountIdFromTxn + ". Cannot create transaction."));
        
      
        txn.setAccount(existingAccount);
   

        txn.setTxnId(UUID.randomUUID());
        txn.setTimeStamp(LocalDateTime.now());
        
        Transaction savedTxn = transactionRepository.save(txn);
        
      
        FraudPrediction fraudPrediction = predictionService.predict(savedTxn);
        

        if (fraudPrediction != null) {
            System.out.println("ML Prediction Service returned: isFraud=" + fraudPrediction.getIsFraud()
        + ", isFlagged=" + fraudPrediction.getIsFlagged());
            savedTxn.setIsFraud(fraudPrediction.getIsFraud());
            savedTxn.setIsFlagged(fraudPrediction.getIsFlagged());
            transactionRepository.save(savedTxn); 
            if (savedTxn.getIsFraud() || savedTxn.getIsFlagged()) {
                accountRepository.incrementFraudTxns(existingAccount.getAccountId());
                Alert alert = new Alert();
                alert.setAlertId(UUID.randomUUID());
                alert.setTransaction(savedTxn);
                alert.setSeverity(fraudPrediction.getIsFraud()?"HIGH":"MEDIUM");
                alert.setReason("ML model predicted");
                alert.setStatus("BLOCKED");
                alert.setCreatedAt(LocalDateTime.now());
                alert.setReason(null);;
                alertRepository.save(alert);
            }
          
            accountRepository.incrementTotalTxns(existingAccount.getAccountId());
        } else {
            System.out.println("ML Prediction Service returned null. Fraud/Flagged status not updated for transaction: " + savedTxn.getTxnId());
            
            accountRepository.incrementTotalTxns(existingAccount.getAccountId());
        }
        return savedTxn;
    }

    public Optional<Transaction>getTransactionById(UUID txnId){
        return transactionRepository.findById(txnId);
    }
    
    public List<Transaction>getAllTransactions(){
        return transactionRepository.findAll();
    }
    
    public List<Transaction>getTransactionByAccount(UUID accountId){
        return transactionRepository.findByAccount_AccountId(accountId);
    }

    public List<Transaction>getTxnByAccountIsFraudTrue(UUID accountId){
        return transactionRepository.findByAccount_AccountIdAndIsFraudTrue(accountId);
    }

    public List<Transaction>getFraudTransaction(){
        return transactionRepository.findByIsFraudTrue();
    }

    public List<Transaction>getFlaggedTransaction(){
        return transactionRepository.findByIsFlaggedTrue();
    }

    public List<Transaction>getTransactionByStatus(String status){
        return transactionRepository.findByStatus(status);
    }

    public void updateStatus(UUID txnId, String status) {
        transactionRepository.updateStatus(txnId, status);
    }

    public void markAsFraud(UUID txnId) {
        transactionRepository.markAsFraud(txnId);
    }
    public BigDecimal getTotalRevenue(){
        return transactionRepository.getRevenue();
    }
    public BigDecimal getResolvedAmount(){
        return transactionRepository.getResolvedRevenue();
    }
    public BigDecimal getTotalFraud(){
        return transactionRepository.getFraud();
    }
    public BigDecimal getTotalTransactionAmount() {
        return transactionRepository.getTotalTransactionAmount();
    }
    public List<Map<String,Object>> getFraudsByCity(){
        List<Object[]> results = transactionRepository.getFraudByCity();
        List<Map<String,Object>> data = new ArrayList<>();
        for(Object[] row : results){
            Map<String,Object> entry = new HashMap<>();
            entry.put("location",row[0]);
            entry.put("fraudLoss",row[1]);
            data.add(entry);
        }
        return data;
    }
    public void backfillAlerts(){
        List<Transaction> fraudTxns = transactionRepository.findByIsFraudTrue();
        for(Transaction txn : fraudTxns){
            List<Alert> existingAlerts = alertRepository.findByTransaction_TxnId(txn.getTxnId());
            if(!existingAlerts.isEmpty()) continue;
            String severity;
            double amount = txn.getAmount().doubleValue();
            if(amount>=100000){
                severity="CRITICAL";
            }
            else if(amount>=50000){
                severity="HIGH";
            }
            else if(amount>=10000){
                severity="MEDIUM";
            }
            else{
                severity="LOW";
            }
            Alert alert = new Alert();
            alert.setAlertId(UUID.randomUUID());
            alert.setCreatedAt(txn.getTimeStamp());
            alert.setStatus("UNRESOLVED");
            alert.setSeverity(severity);
            alert.setReason("Backfilled from past transactions");
            alert.setTransaction(txn);
            alertRepository.save(alert);
            accountService.incrementFraudTxns(txn.getAccount().getAccountId());
        }
        System.out.println("alerts successfully backfilled");
    }
    public List<Map<String,Object>> getPaymentMethods(){
        List<Object[]> results = transactionRepository.getPaymentMethodCounts();
        List<Map<String,Object>> methodStats = new ArrayList<>();
        for(Object[] row: results){
            Map<String,Object> data = new HashMap<>();
            data.put("method",(String) row[0]);
            data.put("count",(Long) row[1]);
            methodStats.add(data);
        }
        return methodStats;
    }
    public List<Map<String, Object>> getTxnByMethod() {
    List<Object[]> rawResults = transactionRepository.getAmountByMethod(); 
    List<Map<String, Object>> formattedResults = new ArrayList<>();

    for (Object[] row : rawResults) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("method", row[0]);
        entry.put("volume", ((BigDecimal) row[1]).longValue()); 
        formattedResults.add(entry);
    }

    return formattedResults;
    }

    public List<Map<String,Object>> getRevenueFraud(){
        List<Object[]> results = transactionRepository.getRevenueByHour();
        List<Map<String,Object>> data= new ArrayList<>();
        for(Object[] row: results){
            Map<String,Object> entry = new HashMap<>();
            entry.put("date",row[0].toString());
            entry.put("revenue",((BigDecimal)row[1]).longValue());
            entry.put("fraudLoss",((BigDecimal)row[2]).longValue());
            data.add(entry);
        }
        return data;
    }
}