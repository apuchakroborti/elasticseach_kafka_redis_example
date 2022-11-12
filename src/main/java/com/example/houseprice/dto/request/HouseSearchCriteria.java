package com.example.houseprice.dto.request;

import com.example.houseprice.es.enums.EsSearchQueryType;
import lombok.Data;

@Data
public class HouseSearchCriteria {
    private Long id;
    private Integer accommodates;
    private String  bedType;
    private Double bathrooms;
    private Integer bedrooms;
    private String cityCode;
    private Boolean  cleaningFree;
    private Boolean hostHasProfilePic;
    private String description;
    private String nameOrDescription;

    private EsSearchQueryType esSearchQueryType;
}