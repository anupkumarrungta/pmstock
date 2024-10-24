package org.rungta.pmstock.kiteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class KiteResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private Object data;

    // Default constructor
    public KiteResponse() {}

    // Parameterized constructor
    public KiteResponse(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "KiteResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}