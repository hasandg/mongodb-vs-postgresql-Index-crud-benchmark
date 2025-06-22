package com.hasandag.performance.automated.model;

import jakarta.persistence.*;
import org.hibernate.proxy.*;

import java.time.*;
import java.util.*;

@Entity
@Table(name = "automated_test_results")
public class AutomatedTestResult  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(name = "test_phase")
    private String testPhase;

    @Column(name = "execution_order")
    private int executionOrder;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "operation")
    private String operation;

    @Column(name = "record_count")
    private int recordCount;

    @Column(name = "mongodb_time")
    private long mongoDbTime;

    @Column(name = "postgres_time")
    private long postgresTime;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "last_retry_time")
    private LocalDateTime lastRetryTime;

    @Column(name = "total_execution_time")
    private long totalExecutionTime;

    @Column(name = "average_execution_time")
    private double averageExecutionTime;

    @Column(name = "success_rate")
    private double successRate;

    @Column(name = "status")
    private String status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "notes")
    private String notes;

    public AutomatedTestResult() {
    }

    public AutomatedTestResult(Long id, String testPhase, int executionOrder, int retryCount, String operation, int recordCount, long mongoDbTime, long postgresTime, LocalDateTime timestamp, LocalDateTime lastRetryTime, long totalExecutionTime, double averageExecutionTime, double successRate, String status, String errorMessage, String notes) {
        this.id = id;
        this.testPhase = testPhase;
        this.executionOrder = executionOrder;
        this.retryCount = retryCount;
        this.operation = operation;
        this.recordCount = recordCount;
        this.mongoDbTime = mongoDbTime;
        this.postgresTime = postgresTime;
        this.timestamp = timestamp;
        this.lastRetryTime = lastRetryTime;
        this.totalExecutionTime = totalExecutionTime;
        this.averageExecutionTime = averageExecutionTime;
        this.successRate = successRate;
        this.status = status;
        this.errorMessage = errorMessage;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestPhase() {
        return testPhase;
    }

    public void setTestPhase(String testPhase) {
        this.testPhase = testPhase;
    }

    public int getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(int executionOrder) {
        this.executionOrder = executionOrder;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
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

    public LocalDateTime getLastRetryTime() {
        return lastRetryTime;
    }

    public void setLastRetryTime(LocalDateTime lastRetryTime) {
        this.lastRetryTime = lastRetryTime;
    }

    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public void setTotalExecutionTime(long totalExecutionTime) {
        this.totalExecutionTime = totalExecutionTime;
    }

    public double getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public void setAverageExecutionTime(double averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @PrePersist
    protected void onCreate() {
        if (lastRetryTime == null) {
            lastRetryTime = LocalDateTime.now();
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public void incrementRetryCount() {
        this.retryCount++;
        this.lastRetryTime = LocalDateTime.now();
    }

    public void updateMetrics(long executionTime) {
        this.totalExecutionTime += executionTime;
        this.averageExecutionTime = (double) totalExecutionTime / (retryCount + 1);
        this.successRate = "SUCCESS".equals(getStatus()) ? 1.0 : 0.0;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AutomatedTestResult that = (AutomatedTestResult) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}