package com.example.houseprice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "LOOKUP_CANCELLATION_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancellationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type", unique = true)
    private String type;

    public CancellationType(String type){
        this.type = type;
    }
}
