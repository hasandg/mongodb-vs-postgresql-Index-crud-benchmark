package com.hasandag.mongovspostgres.repository;

import com.hasandag.mongovspostgres.model.MongoProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoProductRepository extends MongoRepository<MongoProduct, String> {

    List<MongoProduct> findByName(String name);
    List<MongoProduct> findByCategory(String category);
    List<MongoProduct> findByCategoryAndName(String category, String name);
} 