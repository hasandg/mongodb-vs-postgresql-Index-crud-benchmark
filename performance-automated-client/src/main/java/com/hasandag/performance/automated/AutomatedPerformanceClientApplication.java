package com.hasandag.performance.automated;

import com.hasandag.performance.automated.service.AutomatedTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutomatedPerformanceClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutomatedPerformanceClientApplication.class, args);
    }
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public CommandLineRunner exportMetricsOnStartup(AutomatedTestService automatedTestService) {
        return args -> {
            for (String arg : args) {
                if (arg.equals("--export-metrics")) {
                    log.info("Exporting metrics on startup based on command line argument");
                    automatedTestService.exportAllMetrics();
                }
            }
        };
    }
} 