package com.example.houseprice.exceptions;

public class HousePricesNotFoundException extends Exception {

    public HousePricesNotFoundException(String message) {
        super(message);
    }

    public HousePricesNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public HousePricesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
