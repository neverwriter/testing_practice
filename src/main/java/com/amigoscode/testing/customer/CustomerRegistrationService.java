package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
public class CustomerRegistrationService {

        private final CustomerRegistrationRequest request;

    @Autowired
    public CustomerRegistrationService(CustomerRegistrationRequest request) {
        this.request = request;
    }


    public void registerNewCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        System.out.println("Hello");
        // 1. PhoneNumber is taken
        // 2. if taken lets check if belong to same customer
        // - 2.1 if yes return
        // - 2.2 thrown an exception
        // 3. Save customer
    }
}
