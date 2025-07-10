package com.finops.finopsbk.service;

import com.finops.finopsbk.entity.Transaction;
import com.finops.finopsbk.dto.FraudPrediction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID; 

@Service
public class PredictionService {

    private final String ML_API_URL = "https://finops-1.onrender.com/predict"; 

    public FraudPrediction predict(Transaction txn) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> payload = new HashMap<>();
           
            payload.put("request_id", UUID.randomUUID().toString()); 
            payload.put("account_id", txn.getAccount().getAccountId().toString());
            payload.put("amount", txn.getAmount());
            payload.put("method", txn.getMethod());
            payload.put("location", txn.getLocation());
            payload.put("device_id", txn.getDeviceId());
            payload.put("timestamp", txn.getTimeStamp().toString()); 
            payload.put("status", txn.getStatus()); 

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<FraudPrediction> response = restTemplate.postForEntity(
                ML_API_URL,
                request,
                FraudPrediction.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.out.println(" ML Service error: " + e.getMessage());
            e.printStackTrace(); 
            return null;
        }
    }
}