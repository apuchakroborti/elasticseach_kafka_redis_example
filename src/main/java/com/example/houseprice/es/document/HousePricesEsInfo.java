package com.example.houseprice.es.document;

import com.example.houseprice.models.*;
import com.example.houseprice.utils.Utils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Document(indexName = "house_prices_textual_info")
@Setting(settingPath = "static/es-settings.json")
@NoArgsConstructor
public class HousePricesEsInfo {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(name = "amenities", type = FieldType.Text)
    private String amenities;

    @Field(name = "description", type = FieldType.Text)
    private String description;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "accommodates", type = FieldType.Integer)
    private Integer accommodates;

    @Field(name = "bathrooms", type = FieldType.Double)
    private Double bathrooms;

    @Field(name = "bed_type", type = FieldType.Text)
    private String bedType;

    @Field(name = "bedrooms", type = FieldType.Double)
    private Double bedrooms;

    @Field(name = "beds", type = FieldType.Double)
    private Double beds;

    @Field(name = "cancellation_policy", type = FieldType.Text)
    private String cancellationPolicy;

    @Field(name = "city", type = FieldType.Text)
    private String cityCode;

    @Field(name = "cleaning_fee", type = FieldType.Integer)
    private Integer  cleaningFee;


    @Field(name = "first_review", type = FieldType.Date)
    private LocalDate firstReview;

    @Field(name = "host_has_profile_pic", type = FieldType.Integer)
    private Integer hostHasProfilePic;

    @Field(name = "host_identity_verified", type = FieldType.Integer)
    private Integer hostIdentityVerified;

    @Field(name = "host_response_rate_percent", type = FieldType.Double)
    private Double hostResponseRatePercent;

    @Field(name = "host_since", type = FieldType.Date)
    private LocalDate hostSince;

    @Field(name = "instant_bookable", type = FieldType.Integer)
    private Integer instantBookable;

    @Field(name = "last_review", type = FieldType.Date)
    private LocalDate lastReview;

    @Field(name = "latitude", type = FieldType.Double)
    private Double latitude;

    @Field(name = "longitude", type = FieldType.Double)
    private Double longitude;

    @Field(name = "neighbourhood", type = FieldType.Text)
    private String neighbourhoodCity;

    @Field(name = "number_of_review", type = FieldType.Integer)
    private Integer numberOfReview;

    @Field(name = "property_type", type = FieldType.Text)
    private String propertyType;

    @Field(name = "review_scores_rating", type = FieldType.Double)
    private Double reviewScoresRating;

    @Field(name = "room_type", type = FieldType.Text)
    private String roomType;

    @Field(name = "thumbnail_url", type = FieldType.Text)
    private String thumbnailUrl;


    @Field(name = "zipcode", type = FieldType.Text)
    private String lookupZipcode;

    @Field(name = "y_number_of_persons_want", type = FieldType.Double)
    private Double yNumberOfPersonsWant;

    public HousePricesEsInfo(HousePrices housePrices){
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
