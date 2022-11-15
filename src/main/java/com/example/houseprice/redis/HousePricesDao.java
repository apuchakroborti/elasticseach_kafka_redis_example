package com.example.houseprice.redis;

import com.example.houseprice.redis.entity.HousePricesRedisInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HousePricesDao {
    public static final String HASH_KEY = "HousePrices";

    @Autowired
    private RedisTemplate template;

    public HousePricesRedisInfo save(HousePricesRedisInfo housePricesRedisInfo){
        template.opsForHash().put(HASH_KEY, housePricesRedisInfo.getId(), housePricesRedisInfo);
        return housePricesRedisInfo;
    }

    public List<HousePricesRedisInfo> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public HousePricesRedisInfo findHousePricesById(Long id){
        System.out.println("called findHousePricesById() from DB");
        return (HousePricesRedisInfo)template.opsForHash().get(HASH_KEY, id);
    }

    public String deleteHousePricesInfo(Long id){
        template.opsForHash().delete(HASH_KEY, id);
        return "House Prices Data removed";
    }
}
