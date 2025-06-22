package com.hasandag.mongovspostgres.controller;

import com.hasandag.mongovspostgres.service.PerformanceTestService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/performance")
public class PerformanceTestController {
    private final PerformanceTestService performanceTestService;

    public PerformanceTestController(PerformanceTestService performanceTestService) {
        this.performanceTestService = performanceTestService;
    }

    @PostMapping("/generate-data/{count}")
    public Map<String, Long> generateTestData(@PathVariable int count) {
        return performanceTestService.generateTestData(count);
    }

    @GetMapping("/read/{count}")
    public Map<String, Long> testReadPerformance(@PathVariable int count) {
        return performanceTestService.testReadPerformance();
    }

    @GetMapping("/read-with-params")
    public Map<String, Long> testReadWithParamsPerformance(@RequestParam(required = false)  String name, @RequestParam(required = false)  String category) {
        return performanceTestService.testReadWithParamPerformance(name, category);
    }

    @PutMapping("/update/{count}")
    public Map<String, Long> testUpdatePerformance(@PathVariable int count) {
        return performanceTestService.testUpdatePerformance(count);
    }

    @DeleteMapping("/delete/{count}")
    public Map<String, Long> testDeletePerformance(@PathVariable int count) {
        return performanceTestService.testDeletePerformance(count);
    }

} 