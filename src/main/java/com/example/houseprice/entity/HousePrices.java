package com.example.houseprice.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "HOUSE_PRICES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HousePrices {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String amenities;
    
    private Integer accommodates;

    private Double bathrooms;

    @ManyToOne
    @JoinColumn(name = "bed_type_id")
    private BedType bedType;

    private Double bedrooms;
    private Double beds;

    @ManyToOne
    @JoinColumn(name = "cancellation_policy_id")
    private CancellationType cancellationPolicy;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityCode cityCode;

    @Column(name = "cleaning_fee")
    private Integer  cleaningFee;
    
    private String description;

    @Column(name = "first_review")
    private LocalDate  firstReview;

    @Column(name = "host_has_profile_pic")
    private Integer hostHasProfilePic;

    @Column(name = "host_identity_verified")
    private Integer hostIdentityVerified;

    @Column(name = "host_response_rate_percent")
    private Double hostResponseRatePercent;

    @Column(name = "host_since")
    private LocalDate hostSince;

    @Column(name = "instant_bookable")
    private Integer instantBookable;

    @Column(name = "last_review")
    private LocalDate lastReview;
    
    private Double latitude;
    private Double longitude;
    
    private String name;

    @ManyToOne
    @JoinColumn(name = "neighbourhood_id")
    private NeighbourhoodCity neighbourhoodCity;

    @Column(name = "number_of_review")
    private Integer numberOfReview;

    @ManyToOne
    @JoinColumn(name = "property_type_id")
    private PropertyType propertyType;

    @Column(name = "review_scores_rating")
    private Double reviewScoresRating;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "zipcode_id")
    private Zipcode zipcode;

    @Column(name = "y_number_of_persons_want")
    private Double yNumberOfPersonsWant;
}
