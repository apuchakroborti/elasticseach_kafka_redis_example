package com.example.houseprice.config;

import com.example.houseprice.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
//        if(customer.getCountry().equals("United States")) {
//            return customer;
//        }else{
//            return null;
//        }
        return customer;
    }
}
