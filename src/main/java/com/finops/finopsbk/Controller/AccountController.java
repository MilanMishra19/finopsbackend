package com.finops.finopsbk.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finops.finopsbk.entity.Account;
import com.finops.finopsbk.service.AccountService;

@RestController 
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account>createAccount(@RequestBody Account account){
        Account created = accountService.createAccount(account);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account>getByAccountId(@PathVariable UUID accountId){
        return accountService.getAccountById(accountId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Account>>getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccount());
    }

    @GetMapping("/risk/{label}")
    public ResponseEntity<List<Account>> getByRiskLabel(@PathVariable String label) {
        return ResponseEntity.ok(accountService.getByRiskLabel(label.toUpperCase()));
    }

    @PatchMapping("/{accountId}/risk")
    public ResponseEntity<String> updateRisk(
            @PathVariable UUID accountId,
            @RequestParam Float score,
            @RequestParam String label) {

        accountService.updateRisk(accountId, score, label.toUpperCase());
        return ResponseEntity.ok("Risk score/label updated successfully.");
    }

     @PatchMapping("/{accountId}/kyc")
    public ResponseEntity<String> updateKyc(
            @PathVariable UUID accountId,
            @RequestParam String status) {

        accountService.updateKycStatus(accountId, status.toUpperCase());
        return ResponseEntity.ok("KYC status updated successfully.");
    }
}

