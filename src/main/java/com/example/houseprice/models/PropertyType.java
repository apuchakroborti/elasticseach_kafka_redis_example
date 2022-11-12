package com.example.houseprice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "LOOKUP_PROPERTY_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", unique = true)
    private String name;
    public PropertyType(String name){
        this.name = name;
    }
}
