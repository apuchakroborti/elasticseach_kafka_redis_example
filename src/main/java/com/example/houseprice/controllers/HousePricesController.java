package com.example.houseprice.controllers;

import com.example.houseprice.dto.APIResponse;
import com.example.houseprice.dto.request.HouseSearchCriteria;
import com.example.houseprice.dto.response.Pagination;
import com.example.houseprice.dto.response.ServiceResponse;
import com.example.houseprice.es.document.HousePricesEsInfo;
import com.example.houseprice.es.service.HousePricesESService;
import com.example.houseprice.exceptions.GenericException;
import com.example.houseprice.dto.HousePricesDto;
import com.example.houseprice.entity.HousePrices;
import com.example.houseprice.services.HousePricesService;
import com.example.houseprice.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/house-prices")
@AllArgsConstructor
@Slf4j
public class HousePricesController {

    //if we use the annotation @AllArgsConstructor no need to autowired
    private final HousePricesService housePricesService;
    private final HousePricesESService housePricesESService;

    @PostMapping("/multipart-file")
    public ResponseEntity<APIResponse> uploadMultiPartFile(@RequestParam("file") MultipartFile file) throws GenericException {
        log.info(HousePricesController.class.getName()+"::uploadMultiPartFile start");

        String message = housePricesService.insertHousePricesFromKaggleDataset(file);
        //{}, variable should be used for memory optimization also
        log.debug(HousePricesController.class.getName()+"::uploadMultiPartFile message: {}", message);

        //Builder design pattern
        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status("SUCCESS")
                .results(message)
                .build();

        log.info(HousePricesController.class.getName()+"::uploadMultiPartFile finish");
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<APIResponse> fromFilePathAndSaveHousePricesData(@RequestBody String filePath) throws GenericException {
        log.info(HousePricesController.class.getName()+"::fromFilePathAndSaveHousePricesData start");

        String message = housePricesService.insertHousePricesFromKDFilePath(filePath);
        log.debug(HousePricesController.class.getName()+"::fromFilePathAndSaveHousePricesData message: {}", message);

        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status("SUCCESS")
                .results(message)
                .build();

        log.info(HousePricesController.class.getName()+"::fromFilePathAndSaveHousePricesData finish");
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    @PostMapping("/save-by-multi-threading")
    public ResponseEntity<APIResponse> fromFilePathAndSaveHousePricesDataAsync(@RequestBody String filePath) throws GenericException {
        log.info(HousePricesController.class.getName()+"::fromFilePathAndSaveHousePricesDataAsync start");

        CompletableFuture<Iterable<HousePrices>> test = housePricesService.saveHousePricesDataAsync(filePath);

        APIResponse< CompletableFuture<Iterable<HousePrices>>> responseDTO = APIResponse
                .<CompletableFuture<Iterable<HousePrices>>>builder()
                .status("SUCCESS")
                .results(test)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/get-all-by-multi-threading", produces = "application/json")
    public ResponseEntity<APIResponse> getAllHousePricesDataAsync() throws GenericException {
        try{
            CompletableFuture<Iterable<HousePrices>> test = housePricesService.findAllHousePricesDataAsync();
            APIResponse<ServiceResponse<Iterable<HousePrices>>> responseDTO = APIResponse
                    .<ServiceResponse<Iterable<HousePrices>>>builder()
                    .status("SUCCESS")
                    .results(new ServiceResponse<>(null, test.get()))
                    .build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e){
         throw new GenericException("Exception occurred while asynchronous data fetching!");
        }
    }

    @GetMapping(value = "/get-all-by-multi-threading-join", produces = "application/json")
    public  ResponseEntity<APIResponse> getAllHousePricesDataAsyncJoin() throws GenericException{
        CompletableFuture<Iterable<HousePrices>> hp1 = housePricesService.findAllHousePricesDataAsync();
        CompletableFuture<Iterable<HousePrices>> hp2 = housePricesService.findAllHousePricesDataAsync();
        CompletableFuture<Iterable<HousePrices>> hp3 = housePricesService.findAllHousePricesDataAsync();
        CompletableFuture.allOf(hp1,hp2,hp3).join();

        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status("SUCCESS")
                .results("Search done")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchHouse(HouseSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException {
        Page<HousePrices>  housePricesPage = housePricesService.getHousePricesList(criteria, pageable);

        ServiceResponse serviceResponse =  new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(housePricesPage.getContent(), HousePricesDto.class),
                new Pagination(housePricesPage.getTotalElements(), housePricesPage.getNumberOfElements(), housePricesPage.getNumber(), housePricesPage.getSize()));

        APIResponse<ServiceResponse> responseDTO = APIResponse
                .<ServiceResponse>builder()
                .status("SUCCESS")
                .results(serviceResponse)
                .build();

        return  new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    @GetMapping("/adv-search")
    public ResponseEntity<APIResponse> searchAllHousePricesEsData(HouseSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException {
        SearchHits<HousePricesEsInfo> searchHits = housePricesESService.advSearchData(criteria, pageable);

        ServiceResponse serviceResponse = new ServiceResponse(Utils.getSuccessResponse(),
                searchHits.getSearchHits(),
                new Pagination(searchHits.getTotalHits(), searchHits.getSearchHits().size(), pageable.getPageNumber(), pageable.getPageSize()));

        APIResponse<ServiceResponse> responseDTO = APIResponse
                .<ServiceResponse>builder()
                .status("SUCCESS")
                .results(serviceResponse)
                .build();

        return  new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }
    @DeleteMapping("/es-data")
    public ResponseEntity<APIResponse> deleteAllHousePricesEsData() throws GenericException {
        housePricesESService.deleteAll();

        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status("SUCCESS")
                .results("Deleted all data from elasticsearch")
                .build();

        return  new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/hp-pg-data")
    public ResponseEntity<APIResponse> deleteAllHousePricesData() throws GenericException {
        Boolean message = housePricesService.deleteAllHousePricesData();

        APIResponse<Boolean> responseDTO = APIResponse
                .<Boolean>builder()
                .status("SUCCESS")
                .results(message)
                .build();

        return  new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse> createNewHousePrice(@RequestBody @Valid HousePricesDto housePricesDto) throws GenericException{
        log.info(HousePricesController.class.getName()+"::createNewHousePrice request body {}", Utils.jsonAsString(housePricesDto));

        HousePricesDto housePricesResponseDTO = housePricesService.createNewHousePrice(housePricesDto);
        log.debug(HousePricesController.class.getName()+"::createNewHousePrice request body {}", Utils.jsonAsString(housePricesDto));

        //Builder Design pattern
        APIResponse<HousePricesDto> responseDTO = APIResponse
                .<HousePricesDto>builder()
                .status("SUCCESS")
                .results(housePricesResponseDTO)
                .build();

        log.info(HousePricesController.class.getName()+"::createNewHousePrice response {}", Utils.jsonAsString(responseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
