package com.example.houseprice.repository;

import com.example.houseprice.models.PropertyType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupPropertyTypeRepository extends CrudRepository<PropertyType, Integer>{
    Optional<PropertyType> findByName(String name);
}
