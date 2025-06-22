package com.hasandag.mongovspostgres.repository;

import com.hasandag.mongovspostgres.model.PostgresProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostgresProductRepository extends JpaRepository<PostgresProduct, Long> {
    List<PostgresProduct> findByNameAndCategory(String name, String category);

    List<PostgresProduct> findByName(String name);

    List<PostgresProduct> findByCategory(String category);

    // Partial match / LIKE equivalent
    List<PostgresProduct> findByNameContainingIgnoreCase(String name);

    List<PostgresProduct> findByCategoryContainingIgnoreCase(String category);

    List<PostgresProduct> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String name, String category);
} 