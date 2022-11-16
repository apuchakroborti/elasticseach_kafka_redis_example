package com.example.houseprice.controllers;

import com.example.houseprice.dto.APIResponse;
import com.example.houseprice.exceptions.GenericException;
import com.example.houseprice.entity.HousePrices;
import com.example.houseprice.redis.HousePricesDao;
import com.example.houseprice.redis.entity.HousePricesRedisInfo;
import com.example.houseprice.services.HousePricesService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/house-prices/redis")
@EnableCaching
@AllArgsConstructor
public class HousePricesRedisController {

    private final HousePricesService housePricesService;
    private final HousePricesDao housePricesDao;

    /*Using redis as a database*/
    @PostMapping
    public ResponseEntity<APIResponse> saveByIdFromPgToRedis(@RequestParam("id") Long id) throws GenericException {
        HousePrices housePrices = housePricesService.findById(id);
        HousePricesRedisInfo housePricesRedisInfo = new HousePricesRedisInfo(housePrices);

        housePricesRedisInfo = housePricesDao.save(housePricesRedisInfo);

        APIResponse<HousePricesRedisInfo> responseDTO = APIResponse
                .<HousePricesRedisInfo>builder()
                .status("SUCCESS")
                .results(housePricesRedisInfo)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<APIResponse> getAllHousePrices() throws GenericException {
        List<HousePricesRedisInfo> housePricesRedisInfoList = housePricesDao.findAll();

        APIResponse<List<HousePricesRedisInfo>> responseDTO = APIResponse
                .<List<HousePricesRedisInfo>>builder()
                .status("SUCCESS")
                .results(housePricesRedisInfoList)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    //without condition
//    @Cacheable(key = "#id", value = "HousePricesRedisInfo")
    //with condition; value will be cached having id less than or equal to 13150
    @Cacheable(key = "#id", value = "HousePricesRedisInfo", unless = "#result.id > 13150")
    public ResponseEntity<APIResponse> getHousePricesById(@PathVariable("id") Long id) throws GenericException {
        HousePricesRedisInfo housePricesRedisInfo = housePricesDao.findHousePricesById(id);

        APIResponse<HousePricesRedisInfo> responseDTO = APIResponse
                .<HousePricesRedisInfo>builder()
                .status("SUCCESS")
                .results(housePricesRedisInfo)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    //value will be deleted from cache too if deleted from database
    @CacheEvict(key = "#id", value = "HousePricesRedisInfo")
    public ResponseEntity<APIResponse> deleteHousePricesById(@PathVariable("id") Long id) throws GenericException {
        String message = housePricesDao.deleteHousePricesInfo(id);

        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status("SUCCESS")
                .results(message)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
