package com.example.houseprice.repository;

import com.example.houseprice.entity.PgToESTempTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PgToEsRepository extends CrudRepository<PgToESTempTable, Long> {
}
