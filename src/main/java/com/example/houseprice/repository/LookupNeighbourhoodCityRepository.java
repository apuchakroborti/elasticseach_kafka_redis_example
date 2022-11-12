package com.example.houseprice.repository;

import com.example.houseprice.models.NeighbourhoodCity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupNeighbourhoodCityRepository extends CrudRepository<NeighbourhoodCity, Long>{
    Optional<NeighbourhoodCity> findByName(String name);
}
