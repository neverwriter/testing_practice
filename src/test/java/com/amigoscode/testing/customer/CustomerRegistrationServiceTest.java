package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CustomerRegistrationServiceTest {

    private UUID id = UUID.randomUUID();
    private Customer customer = new Customer(id, "Abel", "123456789");

    private  CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

    private CustomerRegistrationService underTest = new CustomerRegistrationService(request);

@Autowired
 private CustomerRepository customerRepository;



    @Test
    void itShouldRegisterNewCustomer() {
        //Given

        customerRepository.save(customer);
        //When

        underTest.registerNewCustomer(request);
        //Then

    }
}