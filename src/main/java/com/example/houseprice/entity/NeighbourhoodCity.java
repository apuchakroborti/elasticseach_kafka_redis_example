package com.example.houseprice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "LOOKUP_NEIGHBOURHOOD_CITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeighbourhoodCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    private String name;

    public NeighbourhoodCity(String name){
        this.name = name;
    }
}
