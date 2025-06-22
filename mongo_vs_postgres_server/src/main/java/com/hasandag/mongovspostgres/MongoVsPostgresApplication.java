package com.hasandag.mongovspostgres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MongoVsPostgresApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoVsPostgresApplication.class, args);
    }
} 