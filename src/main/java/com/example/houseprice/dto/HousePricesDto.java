package com.example.houseprice.dto;

import com.example.houseprice.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.spi.PropertyType;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousePricesDto implements Serializable {
    private Long id;
    private String amenities;
    private Integer accommodates;
    private Double bathrooms;
    private BedType bedType;
    private Integer bedrooms;
    private Integer beds;
    private CityCode cityCode;
    private Boolean  cleaningFree;
    private String description;
    private LocalDate  firstReview;
    private Boolean hostHasProfilePic;
    private Boolean hostIdentityVerified;
    private Double hostResponseRatePercent;
    private LocalDate hostSince;
    private Boolean instantBookable;
    private LocalDate last_review;
    private Double latitude;
    private Double longitude;
    private String name;
    private NeighbourhoodCity neighbourhoodCity;
    private Integer numberOfReview;
    private PropertyType propertyType;
    private Integer reviewScoresRating;
    private RoomType roomType;
    private String thumbnailUrl;
    private Zipcode zipcode;
    private Integer yNumberOfPersonsWant;
}
