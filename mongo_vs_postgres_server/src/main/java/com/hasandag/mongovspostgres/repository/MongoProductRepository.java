package com.hasandag.mongovspostgres.repository;

import com.hasandag.mongovspostgres.model.MongoProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoProductRepository extends MongoRepository<MongoProduct, String> {

    List<MongoProduct> findByNameStartingWithIgnoreCase(String name);
    List<MongoProduct> findByCategoryStartingWithIgnoreCase(String category);
    List<MongoProduct> findByNameStartingWithIgnoreCaseAndCategoryStartingWithIgnoreCase(String name, String category);
} 