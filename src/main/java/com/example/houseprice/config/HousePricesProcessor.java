package com.example.houseprice.config;

import com.example.houseprice.entity.HousePrices;
import org.springframework.batch.item.ItemProcessor;

public class HousePricesProcessor implements ItemProcessor<HousePrices,HousePrices> {

    @Override
    public HousePrices process(HousePrices housePrices) throws Exception {
        if(housePrices.getCityCode().equals("USA")) {
            return housePrices;
        }else{
            return null;
        }
    }
}
