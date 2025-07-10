package com.finops.finopsbk.Controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finops.finopsbk.dto.TransactionRequest;
import com.finops.finopsbk.entity.Account;
import com.finops.finopsbk.entity.Transaction;
import com.finops.finopsbk.service.AccountService;
import com.finops.finopsbk.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionRequest transactionRequest) { 
        Transaction transaction = new Transaction();
        Account tempAccount = new Account();
        tempAccount.setAccountId(transactionRequest.getAccountId()); // Set the accountId from the DTO
        
        transaction.setAccount(tempAccount); 
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setMethod(transactionRequest.getMethod());
        transaction.setTimeStamp(transactionRequest.getTimestamp());
        transaction.setLocation(transactionRequest.getLocation());
        transaction.setDeviceId(transactionRequest.getDeviceId());
        transaction.setStatus(transactionRequest.getStatus());
        transaction.setIsFraud(transactionRequest.getIsFraud()); 
        transaction.setIsFlagged(transactionRequest.getIsFlagged()); 

        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getByAccount(@PathVariable UUID accountId){
        List<Transaction> txns = transactionService.getTransactionByAccount(accountId);
        return ResponseEntity.ok(txns);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>>getAllTransaction(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
    
    @GetMapping("/type")
    public ResponseEntity<List<Map<String,Object>>>getTxnType(){
        return ResponseEntity.ok(transactionService.getTxnByMethod());
    }
    
    @GetMapping("/method-stats")
    public ResponseEntity<List<Map<String,Object>>> getPaymentMethods(){
        return ResponseEntity.ok(transactionService.getPaymentMethods());
    }

    @GetMapping("/revenue-fraud")
    public  ResponseEntity<List<Map<String,Object>>>getRevenueAndFraud(){
        return ResponseEntity.ok(transactionService.getRevenueFraud());
    }

    @GetMapping("/fraud")
    public ResponseEntity<List<Transaction>> getFraudTransactions(){
        return ResponseEntity.ok(transactionService.getFraudTransaction());
    }

    @GetMapping("/fraud/{accountId}")
    public ResponseEntity<List<Transaction>>getUserFraudTxns(@PathVariable UUID accountId){
        return ResponseEntity.ok(transactionService.getTxnByAccountIsFraudTrue(accountId));
    }

    @PatchMapping("/{txnId}/status")
    public ResponseEntity<String> updateTransactionStatus(@PathVariable UUID txnId,@RequestParam String status){
        transactionService.updateStatus(txnId, status);
        return ResponseEntity.ok("Status updated to: "+status);
    }

    @PatchMapping("/{txnId}/fraud")
    public ResponseEntity<String>markAsFraud(@PathVariable UUID txnId,@RequestParam UUID accountId){
        transactionService.markAsFraud(txnId);
        accountService.incrementFraudTxns(accountId);
        return ResponseEntity.ok("Transaction marked as fraud and account updated");
    }
    @GetMapping("/fraud-city")
    public ResponseEntity<List<Map<String,Object>>>getFraudsByLoc(){
        return ResponseEntity.ok(transactionService.getFraudsByCity());
    }
    @GetMapping("/resolved-revenue")
    public ResponseEntity<Map<String,Object>>getTotalResolved(){
        try{
            BigDecimal total = transactionService.getResolvedAmount();
            System.out.println("total amount recovered: "+total);
            Map<String,Object> response = new HashMap<>();
            response.put("resolved",total!=null?total:BigDecimal.ZERO);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            System.out.println("error in api call: "+e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error","internal server error"));
        }
    }
    @GetMapping("/revenue")
    public ResponseEntity<Map<String,Object>> getTotalRev(){
        try{
            BigDecimal total = transactionService.getTotalRevenue();
            System.out.println("total revenue as of now:"+total);
            Map<String,Object> response = new HashMap<>();
            response.put("rev",total!=null?total:BigDecimal.ZERO);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            System.out.println("error in api call:"+ e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error","internal server error"));
        }
    }
    @GetMapping("/fraud-total")
    public ResponseEntity<Map<String,Object>> getTotalFrd(){
        try{
            BigDecimal total = transactionService.getTotalFraud();
            System.out.println("total frauds as of now:"+total);
            Map<String,Object> response = new HashMap<>();
            response.put("fraud",total!=null?total:BigDecimal.ZERO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("error in your api call"+e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error","internal server error"));
        }
    }
    @GetMapping("/total")
public ResponseEntity<Map<String, Object>> getTotalTransactionAmount() {
    System.out.println("📥 Hit /api/transactions/total");

    try {
        BigDecimal total = transactionService.getTotalTransactionAmount();
        System.out.println("✅ Total calculated: " + total);

        Map<String, Object> response = new HashMap<>();
        response.put("total", total != null ? total : BigDecimal.ZERO);

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        System.err.println("❌ Error in /api/transactions/total: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
    }
}
}
