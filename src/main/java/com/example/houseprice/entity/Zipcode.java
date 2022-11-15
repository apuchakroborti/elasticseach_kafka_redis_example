package com.example.houseprice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "LOOKUP_ZIPCODE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Zipcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code", unique = true)
    private String code;

    public Zipcode(String code){
        this.code = code;
    }
}
