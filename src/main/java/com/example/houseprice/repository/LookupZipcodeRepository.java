package com.example.houseprice.repository;

import com.example.houseprice.models.Zipcode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupZipcodeRepository extends CrudRepository<Zipcode, Long>{
    Optional<Zipcode> findByCode(String code);
}
