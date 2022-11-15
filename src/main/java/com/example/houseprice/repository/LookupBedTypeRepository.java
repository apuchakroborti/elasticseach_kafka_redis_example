package com.example.houseprice.repository;

import com.example.houseprice.entity.BedType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupBedTypeRepository extends CrudRepository<BedType, Integer>{
    Optional<BedType> findByType(String type);
}
