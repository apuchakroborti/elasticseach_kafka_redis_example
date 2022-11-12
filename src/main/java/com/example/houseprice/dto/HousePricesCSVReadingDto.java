package com.example.houseprice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HousePricesCSVReadingDto {
    private String id;
    private String amenities;
    private String accommodates;
    private String bathrooms;
    private String bedType;
    private String bedrooms;
    private String beds;
    private String cancellationPolicy;
    private String city;
    private String cleaningFee;
    private String description;
    private String firstReview;
    private String hostHasProfilePic;
    private String hostIdentityVerified;
    private String hostResponseRatePercent;
    private String hostSince;
    private String instantBookable;
    private String lastReview;
    private String latitude;
    private String longitude;
    private String name;
    private String neighbourhood;
    private String numberOfReview;
    private String propertyType;
    private String reviewScoresRating;
    private String roomType;
    private String thumbnailUrl;
    private String  lookupZipcode;
    private String yNumberOfPersonsWant;
}
