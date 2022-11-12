package com.example.houseprice.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorModel implements Serializable {
    private String fieldName;
    private String message;
    private String description;

    public ErrorModel(String fieldName, String message, String description){
        this.fieldName = fieldName;
        this.message = message;
        this.description = description;
    }
}
