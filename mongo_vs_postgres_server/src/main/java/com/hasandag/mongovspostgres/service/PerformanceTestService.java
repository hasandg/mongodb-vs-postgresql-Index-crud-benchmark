package com.hasandag.mongovspostgres.service;

import com.hasandag.mongovspostgres.model.MongoProduct;
import com.hasandag.mongovspostgres.model.PostgresProduct;
import com.hasandag.mongovspostgres.repository.MongoProductRepository;
import com.hasandag.mongovspostgres.repository.PostgresProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class PerformanceTestService {
    private final MongoProductRepository mongoRepository;
    private final PostgresProductRepository postgresRepository;
    private final MongoTemplate mongoTemplate;
    private final Random random = new Random();

    public PerformanceTestService(MongoProductRepository mongoRepository,
                                PostgresProductRepository postgresRepository,
                                MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.postgresRepository = postgresRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Map<String, Long> generateTestData(int count) {
        List<MongoProduct> mongoProducts = new ArrayList<>();
        List<PostgresProduct> postgresProducts = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            MongoProduct mongoProduct = new MongoProduct();
            mongoProduct.setName("Product " + (i % 1000));
            mongoProduct.setDescription("Description for product " + i);
            mongoProduct.setPrice(random.nextDouble() * 1000);
            mongoProduct.setStock(random.nextInt(1000));
            mongoProduct.setCategory("Category " + (i % 10));
            mongoProduct.setTags(new String[]{"tag" + (i % 5), "tag" + ((i + 1) % 5)});
            mongoProducts.add(mongoProduct);

            PostgresProduct postgresProduct = new PostgresProduct();
            postgresProduct.setName("Product " + (i % 1000));
            postgresProduct.setDescription("Description for product " + i);
            postgresProduct.setPrice(random.nextDouble() * 1000);
            postgresProduct.setStock(random.nextInt(1000));
            postgresProduct.setCategory("Category " + (i % 10));
            postgresProduct.setTags(new String[]{"tag" + (i % 5), "tag" + ((i + 1) % 5)});
            postgresProducts.add(postgresProduct);
        }

        long mongoStart = System.currentTimeMillis();
        mongoRepository.saveAll(mongoProducts);
        long mongoEnd = System.currentTimeMillis();
        long mongoTime = mongoEnd - mongoStart;
        System.out.println("MongoDB Insert Time: " + mongoTime + "ms");

        long postgresStart = System.currentTimeMillis();
        postgresRepository.saveAll(postgresProducts);
        long postgresEnd = System.currentTimeMillis();
        long postgresTime = postgresEnd - postgresStart;
        System.out.println("PostgreSQL Insert Time: " + postgresTime + "ms");

        Map<String, Long> results = new HashMap<>();
        results.put("mongodb", mongoTime);
        results.put("postgres", postgresTime);
        return results;
    }

    public Map<String, Long> testReadPerformance() {
        long mongoStart = System.currentTimeMillis();
        List<MongoProduct> mongoProducts = mongoRepository.findAll();
        long mongoEnd = System.currentTimeMillis();
        long mongoTime = mongoEnd - mongoStart;
        System.out.println("MongoDB Read Time: " + mongoTime + "ms");

        long postgresStart = System.currentTimeMillis();
        List<PostgresProduct> postgresProducts = postgresRepository.findAll();
        long postgresEnd = System.currentTimeMillis();
        long postgresTime = postgresEnd - postgresStart;
        System.out.println("PostgreSQL Read Time: " + postgresTime + "ms");

        Map<String, Long> results = new HashMap<>();
        results.put("mongodb", mongoTime);
        results.put("postgres", postgresTime);
        return results;
    }

    public Map<String, Long> testReadWithParamPerformance(String name, String category) {
        long mongoStart = System.currentTimeMillis();
        List<MongoProduct> mongoProducts;
        if (name != null && category != null) {
            mongoProducts = mongoRepository.findByCategoryAndName(category, name);
        } else if (name != null) {
            mongoProducts = mongoRepository.findByName(name);
        } else if (category != null) {
            mongoProducts = mongoRepository.findByCategory(category);
        } else {
            mongoProducts = mongoRepository.findAll();
        }
        long mongoEnd = System.currentTimeMillis();
        long mongoTime = mongoEnd - mongoStart;
        System.out.println("MongoDB Read Time: " + mongoTime + "ms" + " Records: " + mongoProducts.size());

        long postgresStart = System.currentTimeMillis();
        List<PostgresProduct> postgresProducts;
        if (name != null && category != null) {
            postgresProducts = postgresRepository.findByCategoryAndName(category, name);
        } else if (name != null) {
            postgresProducts = postgresRepository.findByName(name);
        } else if (category != null) {
            postgresProducts = postgresRepository.findByCategory(category);
        } else {
            postgresProducts = postgresRepository.findAll();
        }
        long postgresEnd = System.currentTimeMillis();
        long postgresTime = postgresEnd - postgresStart;
        System.out.println("PostgreSQL Read Time: " + postgresTime + "ms" + " Records: " + postgresProducts.size());

        Map<String, Long> results = new HashMap<>();
        results.put("mongodb", mongoTime);
        results.put("postgres", postgresTime);
        return results;
    }

    public Map<String, Long> testUpdatePerformance(int count) {
        List<MongoProduct> mongoProducts = mongoRepository.findAll();
        List<PostgresProduct> postgresProducts = postgresRepository.findAll();

        long mongoStart = System.currentTimeMillis();
        for (int i = 0; i < Math.min(count, mongoProducts.size()); i++) {
            MongoProduct product = mongoProducts.get(i);
            product.setPrice(product.getPrice() * 1.1);
            mongoRepository.save(product);
        }
        long mongoEnd = System.currentTimeMillis();
        long mongoTime = mongoEnd - mongoStart;
        System.out.println("MongoDB Update Time: " + mongoTime + "ms");

        long postgresStart = System.currentTimeMillis();
        for (int i = 0; i < Math.min(count, postgresProducts.size()); i++) {
            PostgresProduct product = postgresProducts.get(i);
            product.setPrice(product.getPrice() * 1.1);
            postgresRepository.save(product);
        }
        long postgresEnd = System.currentTimeMillis();
        long postgresTime = postgresEnd - postgresStart;
        System.out.println("PostgreSQL Update Time: " + postgresTime + "ms");

        Map<String, Long> results = new HashMap<>();
        results.put("mongodb", mongoTime);
        results.put("postgres", postgresTime);
        return results;
    }

    @Transactional
    public Map<String, Long> testDeletePerformance(int count) {
        long mongoStart = System.currentTimeMillis();
        long mongoDeleted = mongoTemplate.remove(new Query().limit(count), MongoProduct.class)
                                           .getDeletedCount();
        long mongoTime = System.currentTimeMillis() - mongoStart;
        System.out.println("MongoDB Delete Time: " + mongoTime + "ms");

        long postgresStart = System.currentTimeMillis();
        int postgresDeleted = postgresRepository.deleteFirstN(count);
        long postgresTime = System.currentTimeMillis() - postgresStart;
        System.out.println("PostgreSQL Delete Time: " + postgresTime + "ms");

        Map<String, Long> results = new HashMap<>();
        results.put("mongodb", mongoTime);
        results.put("postgres", postgresTime);
        return results;
    }

} 