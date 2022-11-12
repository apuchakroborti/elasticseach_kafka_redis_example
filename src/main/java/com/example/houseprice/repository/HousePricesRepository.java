package com.example.houseprice.repository;

import com.example.houseprice.models.HousePrices;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousePricesRepository extends CrudRepository<HousePrices, Long>, JpaSpecificationExecutor<HousePrices> {
}
