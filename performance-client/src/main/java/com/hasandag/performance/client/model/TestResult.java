package com.hasandag.performance.client.model;

import java.time.LocalDateTime;

public class TestResult {
    private String operation;
    private int recordCount;
    private long mongoDbTime;
    private long postgresTime;
    private LocalDateTime timestamp;
    private String status;
    private String errorMessage;

    public TestResult() {
    }

    public TestResult(String operation, int recordCount) {
        this.operation = operation;
        this.recordCount = recordCount;
        this.timestamp = LocalDateTime.now();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public long getMongoDbTime() {
        return mongoDbTime;
    }

    public void setMongoDbTime(long mongoDbTime) {
        this.mongoDbTime = mongoDbTime;
    }

    public long getPostgresTime() {
        return postgresTime;
    }

    public void setPostgresTime(long postgresTime) {
        this.postgresTime = postgresTime;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}