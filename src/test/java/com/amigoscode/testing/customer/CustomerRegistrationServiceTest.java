package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRegistrationServiceTest {

    @Autowired
    private CustomerRegistrationService underTest;


    @Test
    void itShouldRegisterNewCustomer() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "123456789");
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        System.out.println(customer.getId()+"   "+customer.getName()+"   "+customer.getPhoneNumber());
        System.out.println(request.getCustomer());
        //When

        //underTest.registerNewCustomer(request);
        //Then

    }
}