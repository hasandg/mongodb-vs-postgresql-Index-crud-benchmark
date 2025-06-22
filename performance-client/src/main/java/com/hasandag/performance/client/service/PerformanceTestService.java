package com.hasandag.performance.client.service;

import com.hasandag.performance.client.model.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PerformanceTestService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final WebClient webClient;
    private final ExecutorService executorService;
    private final List<TestResult> testResults;

    public PerformanceTestService(WebClient webClient) {
        this.webClient = webClient;
        this.executorService = Executors.newFixedThreadPool(5);
        this.testResults = new ArrayList<>();
    }

    public CompletableFuture<TestResult> runTest(String operation, int recordCount) {
        return runTest(operation, recordCount, null, null);
    }

    public CompletableFuture<TestResult> runTest(String operation, int recordCount, String name, String category) {
        return CompletableFuture.supplyAsync(() -> {
            TestResult result = new TestResult(operation, recordCount);
            try {
                String uri = "/api/performance/" + operation + "/" + recordCount;
                String uriWithoutCount = "/api/performance/" + operation;
                Map<String, Long> response;
                
                switch (operation) {
                    case "generate-data":
                        response = webClient.post()
                                .uri(uri)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                                .block();
                        break;
                    case "read":
                        response = webClient.get()
                                .uri(uri)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                                .block();
                        break;
                    case "read-with-params":
                        response = webClient.get()
                                .uri(uriBuilder -> {
                                    var builder = uriBuilder.path(uriWithoutCount);
                                    if (name != null) {
                                        builder = builder.queryParam("name", name);
                                    }
                                    if (category != null) {
                                        builder = builder.queryParam("category", category);
                                    }
                                    return builder.build();
                                })
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                                .block();
                        break;
                    case "update":
                        response = webClient.put()
                                .uri(uri)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                                .block();
                        break;
                    case "delete":
                        response = webClient.delete()
                                .uri(uri)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                                .block();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operation: " + operation);
                }

                if (response != null) {
                    result.setMongoDbTime(response.get("mongodb"));
                    result.setPostgresTime(response.get("postgres"));
                    result.setStatus("SUCCESS");
                }
            } catch (Exception e) {
                result.setStatus("FAILED");
                result.setErrorMessage(e.getMessage());
                log.error("Error running {} test: {}", operation, e.getMessage());
            }
            testResults.add(result);
            return result;
        }, executorService);
    }

    public List<TestResult> getTestResults() {
        return new ArrayList<>(testResults);
    }

    public void clearResults() {
        testResults.clear();
    }

    public void shutdown() {
        executorService.shutdown();
    }
} 