package com.finops.finopsbk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FraudPrediction {
    @JsonProperty("is_fraud_predicted")
    private boolean isFraud;
    @JsonProperty("is_flagged_simulated")
    private boolean isFlagged;

    public boolean getIsFraud(){
        return isFraud;
    }
    public void setIsFraud(boolean isFraud){
        this.isFraud = isFraud;
    }
    public boolean getIsFlagged(){
        return isFlagged;
    }
    public void setIsFlagged(boolean isFlagged){
        this.isFlagged = isFlagged;
    }
}
