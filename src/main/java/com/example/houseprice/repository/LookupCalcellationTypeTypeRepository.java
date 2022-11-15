package com.example.houseprice.repository;

import com.example.houseprice.entity.CancellationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupCalcellationTypeTypeRepository extends CrudRepository<CancellationType, Integer>{
    Optional<CancellationType> findByType(String type);
}
