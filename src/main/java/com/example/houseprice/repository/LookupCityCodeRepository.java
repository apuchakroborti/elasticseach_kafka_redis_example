package com.example.houseprice.repository;

import com.example.houseprice.models.CityCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupCityCodeRepository extends CrudRepository<CityCode, Integer>{
    Optional<CityCode> getByCode(String code);
}
