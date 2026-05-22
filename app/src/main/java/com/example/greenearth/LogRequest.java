// File: LogRequest.java
package com.example.greenearth;
public class LogRequest {
    private float total_carbon;
    private String notes;

    public LogRequest(float total_carbon, String notes) {
        this.total_carbon = total_carbon;
        this.notes = notes;
    }
}