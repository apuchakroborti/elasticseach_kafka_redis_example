package com.example.houseprice.repository;

import com.example.houseprice.models.BedType;
import com.example.houseprice.models.Zipcode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupBedTypeRepository extends CrudRepository<BedType, Integer>{
    Optional<BedType> findByType(String type);
}
