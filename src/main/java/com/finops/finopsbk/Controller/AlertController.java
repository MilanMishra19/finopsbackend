package com.finops.finopsbk.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page; 

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

import com.finops.finopsbk.entity.Alert;
import com.finops.finopsbk.entity.Analyst;
import com.finops.finopsbk.service.AlertService;
import com.finops.finopsbk.service.AnalystService;
import com.finops.finopsbk.service.TransactionService;
import com.finops.finopsbk.dto.AlertRequest;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    @Autowired
    private AlertService alertService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AnalystService analystService;
    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }
    @GetMapping("/severity")
    public ResponseEntity<List<Map<String,Object>>> getAlertsBySeverity(){
        return ResponseEntity.ok(alertService.getAlertsSeverity());
    }
    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert){
        Alert created  = alertService.createAlert(alert);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/unresolved")
    public ResponseEntity<List<Alert>>getUnresolvedAlert(){
        return ResponseEntity.ok(alertService.getAllUnresolved());
    }
    
    @GetMapping("/hourly")
    public ResponseEntity<List<Map<String, Object>>> getAlertsGroupedByHour() {
    return ResponseEntity.ok(alertService.getAlertsByHour());
    }
    
    @GetMapping("/severity/{level}")
    public ResponseEntity<List<Alert>>getAlertBySeverity(@PathVariable String level){
        return ResponseEntity.ok(alertService.getAlertBySeverity(level));
    }

    @GetMapping("/transaction/{txnId}")
    public ResponseEntity<List<Alert>>getByTransaction(@PathVariable UUID txnId){
        return ResponseEntity.ok(alertService.getAlertByTxnId(txnId));
    }
    @GetMapping("/status-distribution")
    public ResponseEntity<List<Map<String,Object>>>getStatusDistribution(){
        return ResponseEntity.ok(alertService.countStatusGrouped());
    }    @PatchMapping("/resolve/{alertId}")
    public ResponseEntity<String>resolveAlert(@PathVariable UUID alertId,@RequestParam String status,@RequestParam String resolvedBy){
        alertService.resolveAlert(alertId, status, resolvedBy);
        return ResponseEntity.ok("Alert resolved with status: "+status);
    }

    @PostMapping("/backfill")
    public ResponseEntity<String>backFillAlerts(){
        transactionService.backfillAlerts();
        return ResponseEntity.ok("all alerts have been updated successfully");
    }
    @GetMapping("/active")
    public ResponseEntity<Page<AlertRequest>> getActiveAlerts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(alertService.getActiveAlerts(page, size));
    }
    
    @GetMapping("/resolved-alerts")
    public ResponseEntity<?> getResolvedAlerts(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        String email = principal.getName();
        Optional<Analyst> analystOpt = analystService.getAnalystByEmail(email);
        if(analystOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Analyst not found");
        }
        UUID analystID = analystOpt.get().getAnalystId();
        List<AlertRequest> resolvedAlerts = alertService.getResolvedAlertRequests(analystID);
        return ResponseEntity.ok(resolvedAlerts);
    }
}
