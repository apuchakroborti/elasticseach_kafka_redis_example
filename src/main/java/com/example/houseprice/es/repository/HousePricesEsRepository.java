package com.example.houseprice.es.repository;

import com.example.houseprice.es.document.HousePricesEsInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousePricesEsRepository extends ElasticsearchRepository<HousePricesEsInfo, Long>{
}
