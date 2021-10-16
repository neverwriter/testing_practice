package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest reguest) {

        // 1. PhoneNumber is taken
        // 2. if taken lets check if belong to same customer
        // - 2.1 if yes return
        // - 2.2 thrown an exception
        // 3. Save customer
    }
}
