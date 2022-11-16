package com.example.houseprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HousePriceFullTextSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(HousePriceFullTextSearchApplication.class, args);
	}

}
