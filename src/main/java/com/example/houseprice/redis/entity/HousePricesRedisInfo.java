package com.example.houseprice.redis.entity;

import com.example.houseprice.models.HousePrices;
import com.example.houseprice.utils.Utils;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("HousePricesRedisInfo")
public class HousePricesRedisInfo implements Serializable {
    @Id
    private Long id;
    private String amenities;
    private String description;
    private String name;
    private Integer accommodates;
    private Double bathrooms;
    private String bedType;
    private Double bedrooms;
    private Double beds;
    private String cancellationPolicy;
    private String cityCode;
    private Integer  cleaningFee;
    private LocalDate firstReview;
    private Integer hostHasProfilePic;
    private Integer hostIdentityVerified;
    private Double hostResponseRatePercent;
    private LocalDate hostSince;
    private Integer instantBookable;
    private LocalDate lastReview;
    private Double latitude;
    private Double longitude;
    private String neighbourhoodCity;
    private Integer numberOfReview;
    private String propertyType;
    private Double reviewScoresRating;
    private String roomType;
    private String thumbnailUrl;
    private String lookupZipcode;
    private Double yNumberOfPersonsWant;
    public HousePricesRedisInfo(HousePrices housePrices){
        Utils.copyProperty(housePrices, this);
        if(housePrices.getBedType()!=null){
            this.bedType =housePrices.getBedType().getType();
        }
        if(housePrices.getCancellationPolicy()!=null){
            this.cancellationPolicy = housePrices.getCancellationPolicy().getType();
        }
        if(housePrices.getRoomType()!=null){
            this.roomType = housePrices.getRoomType().getType();
        }
        if(housePrices.getZipcode()!=null){
            this.lookupZipcode =housePrices.getZipcode().getCode();
        }
        if(housePrices.getCityCode()!=null){
            this.cityCode =housePrices.getCityCode().getCode();
        }
        if(housePrices.getNeighbourhoodCity()!=null){
            this.neighbourhoodCity = housePrices.getNeighbourhoodCity().getName();
        }
        if(housePrices.getPropertyType()!=null){
            this.propertyType = housePrices.getPropertyType().getName();
        }
    }
}
