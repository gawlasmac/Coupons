package com.example.coupons;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "Customers")
public interface CustomerApiClient {

    @GetMapping(value = "/customers")
    ResponseEntity<List<Customer>> getAllCustomers();

    @GetMapping(value = "/customers/{id}")
    ResponseEntity<Customer> getCustomerById(@PathVariable Long id);

}
