package com.hasandag.mongovspostgres.repository;

import com.hasandag.mongovspostgres.model.PostgresProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PostgresProductRepository extends JpaRepository<PostgresProduct, Long> {

    List<PostgresProduct> findByNameStartingWithIgnoreCase(String name);
    List<PostgresProduct> findByCategoryStartingWithIgnoreCase(String category);
    List<PostgresProduct> findByCategoryAndName(String category, String name);

    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query(value = "DELETE FROM postgres_product WHERE id IN (SELECT id FROM postgres_product LIMIT :count)", nativeQuery = true)
    int deleteFirstN(@Param("count") int count);
}