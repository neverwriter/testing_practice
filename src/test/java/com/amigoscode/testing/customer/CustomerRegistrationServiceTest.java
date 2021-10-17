package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CustomerRegistrationServiceTest {
    

@Autowired
 private CustomerRepository customerRepository;



    @Test
    void itShouldRegisterNewCustomer() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "123456789");
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        CustomerRegistrationService underTest = new CustomerRegistrationService(request);
        customerRepository.save(customer);
        //When

        underTest.registerNewCustomer(request);
        //Then

    }
}