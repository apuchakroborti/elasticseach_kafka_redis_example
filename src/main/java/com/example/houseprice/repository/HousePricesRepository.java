package com.example.houseprice.repository;

import com.example.houseprice.entity.HousePrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO need to check details about the difference between Crud and Jpa repository
@Repository
public interface HousePricesRepository extends JpaRepository<HousePrices, Long>, CrudRepository<HousePrices, Long>, JpaSpecificationExecutor<HousePrices> {
    List<HousePrices> findAll();
}
