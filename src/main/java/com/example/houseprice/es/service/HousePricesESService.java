package com.example.houseprice.es.service;

import com.example.houseprice.dto.request.HouseSearchCriteria;
import com.example.houseprice.es.document.HousePricesEsInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface HousePricesESService {
    void save(final HousePricesEsInfo housePricesEsInfo);
    HousePricesEsInfo findById(final Long id);
    SearchHits<HousePricesEsInfo> advSearchData(HouseSearchCriteria criteria, Pageable pageable);
    void deleteAll();
}
