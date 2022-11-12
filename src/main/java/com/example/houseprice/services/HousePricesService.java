package com.example.houseprice.services;

import com.example.houseprice.dto.request.HouseSearchCriteria;
import com.example.houseprice.dto.response.ServiceResponse;
import com.example.houseprice.exceptions.GenericException;
import com.example.houseprice.models.HousePrices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface HousePricesService {
    ResponseEntity<ServiceResponse> insertHousePricesFromKaggleDataset(MultipartFile file) throws GenericException;
    ResponseEntity<ServiceResponse> insertHousePricesFromKDFilePath(String filePath) throws GenericException;
    Page<HousePrices> getHousePricesList(HouseSearchCriteria criteria, Pageable pageable) throws GenericException;
}