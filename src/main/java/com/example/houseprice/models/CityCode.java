package com.example.houseprice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "LOOKUP_CITY_CODE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code", unique = true)
    private String code;

    public CityCode(String code){
        this.code = code;
    }
}
