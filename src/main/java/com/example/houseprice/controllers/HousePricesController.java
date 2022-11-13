package com.example.houseprice.controllers;

import com.example.houseprice.dto.request.HouseSearchCriteria;
import com.example.houseprice.dto.response.Pagination;
import com.example.houseprice.dto.response.ServiceResponse;
import com.example.houseprice.es.document.HousePricesEsInfo;
import com.example.houseprice.es.service.HousePricesESService;
import com.example.houseprice.exceptions.GenericException;
import com.example.houseprice.dto.HousePricesDto;
import com.example.houseprice.models.HousePrices;
import com.example.houseprice.services.HousePricesService;
import com.example.houseprice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/house-prices")
public class HousePricesController {

    private final HousePricesService housePricesService;
    private final HousePricesESService housePricesESService;

    @Autowired
    HousePricesController(HousePricesService housePricesService,
                          HousePricesESService housePricesESService){
        this.housePricesService = housePricesService;
        this.housePricesESService = housePricesESService;
    }

    @PostMapping("/multipart-file")
    public ResponseEntity<ServiceResponse> uploadMultiPartFile(@RequestParam("file") MultipartFile file) throws GenericException {
        return housePricesService.insertHousePricesFromKaggleDataset(file);
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> fromFilePathAndSaveHousePricesData(@RequestBody String filePath) throws GenericException {
        return housePricesService.insertHousePricesFromKDFilePath(filePath);

    }
    @PostMapping("/save-by-multi-threading")
    public ResponseEntity<ServiceResponse> fromFilePathAndSaveHousePricesDataAsync(@RequestBody String filePath) throws GenericException {
        housePricesService.saveHousePricesDataAsync(filePath);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping(value = "/get-all-by-multi-threading", produces = "application/json")
    public CompletableFuture<ResponseEntity> getAllHousePricesDataAsync(@RequestBody String filePath) throws GenericException {
//        housePricesService.saveHousePricesDataCompletableFuture(filePath);
        return housePricesService.findAllHousePricesDataAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/search")
    public ServiceResponse<Page<HousePricesDto>> searchHouse(HouseSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException {
        Page<HousePrices>  housePricesPage = housePricesService.getHousePricesList(criteria, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(housePricesPage.getContent(), HousePricesDto.class),
                new Pagination(housePricesPage.getTotalElements(), housePricesPage.getNumberOfElements(), housePricesPage.getNumber(), housePricesPage.getSize()));
    }
    @GetMapping("/adv-search")
    public ServiceResponse searchAllHousePricesEsData(HouseSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException {
        SearchHits<HousePricesEsInfo> searchHits = housePricesESService.advSearchData(criteria, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                searchHits.getSearchHits(),
                new Pagination(searchHits.getTotalHits(), searchHits.getSearchHits().size(), pageable.getPageNumber(), pageable.getPageSize()));
    }
    @DeleteMapping("/es-data")
    public ServiceResponse deleteAllHousePricesEsData() throws GenericException {
        housePricesESService.deleteAll();
        return  new ServiceResponse(null, true);
    }
}
