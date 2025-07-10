package com.finops.finopsbk.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finops.finopsbk.entity.Account;
import com.finops.finopsbk.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired 
    private AccountRepository accountRepository;
    public Account createAccount(Account account){
        account.setAccountId(UUID.randomUUID());
        return accountRepository.save(account);
    }

    public Optional<Account>getAccountById(UUID id){
        return accountRepository.findById(id);
    }

    public List<Account>getAllAccount(){
        return accountRepository.findAll();
    }

    public Optional<Account>getByAccountNumber(String number){
        return accountRepository.findByAccountNumber(number);
    }

    public List<Account>getByKycStatus(String kycStatus){
        return accountRepository.findByKycStatus(kycStatus);
    }

    public List<Account>getByRiskLabel(String label){
        return accountRepository.findByRiskLabel(label);
    }

    public void updateKycStatus(UUID accountId,String status){
        accountRepository.updateKycStatus(accountId, status);
    }

    public void updateRisk(UUID accountId,Float score,String label){
        accountRepository.updateRisk(accountId, score, label);
    }

    public void incrementTotalTxns(UUID accountId){
        accountRepository.incrementTotalTxns(accountId);
    }
    public void decrementFraudTxns(UUID accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            // Ensure the count doesn't go below zero
            if (account.getFraudTxns() > 0) {
                account.setFraudTxns(account.getFraudTxns() - 1);
                accountRepository.save(account);
            }
        } else {
            throw new RuntimeException("Account with ID " + accountId + " not found.");
        }
    }
    public void incrementFraudTxns(UUID accountId){
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            account.setFraudTxns(account.getFraudTxns() + 1); 
            accountRepository.save(account);
        }
    }

}
