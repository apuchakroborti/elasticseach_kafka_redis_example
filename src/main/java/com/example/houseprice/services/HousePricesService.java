package com.example.houseprice.services;

import com.example.houseprice.dto.HousePricesDto;
import com.example.houseprice.dto.request.HouseSearchCriteria;
import com.example.houseprice.exceptions.GenericException;
import com.example.houseprice.entity.HousePrices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface HousePricesService {
    CompletableFuture<Iterable<HousePrices>> saveHousePricesDataAsync(String file) throws GenericException;
    CompletableFuture<Iterable<HousePrices>> findAllHousePricesDataAsync() throws GenericException;

    String insertHousePricesFromKaggleDataset(MultipartFile file) throws GenericException;
    String insertHousePricesFromKDFilePath(String filePath) throws GenericException;
    Page<HousePrices> getHousePricesList(HouseSearchCriteria criteria, Pageable pageable) throws GenericException;
    Boolean deleteAllHousePricesData() throws GenericException;
    HousePrices findById(Long id) throws GenericException;

    HousePricesDto createNewHousePrice(HousePricesDto housePricesDto) throws GenericException;
    List<HousePricesDto> findAll() throws GenericException;
}
