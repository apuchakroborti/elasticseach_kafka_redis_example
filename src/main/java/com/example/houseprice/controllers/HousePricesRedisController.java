package com.example.houseprice.controllers;

import com.example.houseprice.dto.response.ServiceResponse;
import com.example.houseprice.exceptions.GenericException;
import com.example.houseprice.entity.HousePrices;
import com.example.houseprice.redis.HousePricesDao;
import com.example.houseprice.redis.entity.HousePricesRedisInfo;
import com.example.houseprice.services.HousePricesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/house-prices/redis")
@EnableCaching
public class HousePricesRedisController {

    private final HousePricesService housePricesService;

    private HousePricesDao housePricesDao;

    @Autowired
    HousePricesRedisController(HousePricesService housePricesService,
                               HousePricesDao housePricesDao){
        this.housePricesService = housePricesService;
        this.housePricesDao = housePricesDao;
    }

    /*Using redis as a database*/
    @PostMapping
    public ResponseEntity<ServiceResponse> saveByIdFromPgToRedis(@RequestParam("id") Long id) throws GenericException {
        HousePrices housePrices = housePricesService.findById(id);
        if(housePrices==null)return ResponseEntity.ok(new ServiceResponse(null, "House Prices Data not found with this if: "+id));
        HousePricesRedisInfo housePricesRedisInfo = new HousePricesRedisInfo(housePrices);
        return ResponseEntity.ok(new ServiceResponse(null, housePricesDao.save(housePricesRedisInfo)));
    }

    @GetMapping(produces = "application/json")
    public List<HousePricesRedisInfo> getAllHousePrices() throws GenericException {
        return housePricesDao.findAll();
    }
    @GetMapping(value = "/{id}", produces = "application/json")
    //without condition
//    @Cacheable(key = "#id", value = "HousePricesRedisInfo")
    //with condition; value will be cached having id less than or equal to 13150
    @Cacheable(key = "#id", value = "HousePricesRedisInfo", unless = "#result.id > 13150")
    public HousePricesRedisInfo getHousePricesById(@PathVariable("id") Long id) throws GenericException {
        return housePricesDao.findHousePricesById(id);
    }

    @DeleteMapping(value = "/{id}")
    //value will be deleted from cache too if deleted from database
    @CacheEvict(key = "#id", value = "HousePricesRedisInfo")
    public String deleteHousePricesById(@PathVariable("id") Long id) throws GenericException {
        return housePricesDao.deleteHousePricesInfo(id);
    }

}
