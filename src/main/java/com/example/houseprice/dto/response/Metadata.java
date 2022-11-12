package com.example.houseprice.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Data
public class Metadata implements Serializable {
    private HttpStatus status;
    private Integer code;
    private List<ErrorModel> errorList;
    public Metadata(HttpStatus status, List<ErrorModel> errorList){
        this.status = status;
        this.code = status.value();
        this.errorList = errorList;
    }
    public Metadata(HttpStatus status){
        this.status = status;
        this.code = status.value();
        this.errorList = errorList;
    }
}