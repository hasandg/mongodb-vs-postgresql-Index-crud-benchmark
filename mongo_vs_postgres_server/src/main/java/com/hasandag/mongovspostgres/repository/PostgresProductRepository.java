package com.hasandag.mongovspostgres.repository;

import com.hasandag.mongovspostgres.model.PostgresProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostgresProductRepository extends JpaRepository<PostgresProduct, Long> {

    List<PostgresProduct> findByNameStartingWith(String name);
    List<PostgresProduct> findByCategoryStartingWith(String category);
    List<PostgresProduct> findByCategoryStartingWithAndNameStartingWith(String category, String name);
}