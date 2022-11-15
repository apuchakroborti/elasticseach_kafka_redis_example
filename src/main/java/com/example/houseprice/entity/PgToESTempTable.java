package com.example.houseprice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PG_ES_TEMP_TABLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PgToESTempTable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "HOUSE_PRICE_TABLE_ID", nullable = false)
    private Long housePricesId;

    @Column(name = "AMENITIES")
    private String amenities;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "name")
    private String name;

    public PgToESTempTable(Long housePricesId, String amenities, String description, String name){
        this.housePricesId = housePricesId;
        this.amenities = amenities;
        this.description = description;
        this.name = name;
    }
}
