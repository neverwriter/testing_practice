package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRegistrationServiceTest {


    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void itShouldRegisterNewCustomer() {
        //Given
        UUID id1 = UUID.randomUUID(), id2 = UUID.randomUUID();

        Customer customer1 = new Customer(id1, "Abel", "123456789"), customer2 = new Customer(id2, "Edgar", "600600600");

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer1);
        CustomerRegistrationService underTest = new CustomerRegistrationService(request, customerRepository);

        customerRepository.save(customer2);

        //When

        underTest.registerNewCustomer(request);

        //Then
        Optional<Customer> optionalCustomer = customerRepository.findById(id2);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualToComparingFieldByField(customer2);
                });
    }

    @Test
    void itShouldNotRegisterNewCustomer() {

        //Given
        UUID id1 = UUID.randomUUID(), id2 = UUID.randomUUID();
        Customer customer1 = new Customer(id1, "Abel", "123456789"), customer2 = new Customer(id2, "Abel", "123456789");



        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer1);
        CustomerRegistrationService underTest = new CustomerRegistrationService(request, customerRepository);

        customerRepository.save(customer2);

        //When

        underTest.registerNewCustomer(request);

        //Then

    }

    @Test
    void itShouldThrowAnException() {

        //Given
        boolean exception = false;
        UUID id1 = UUID.randomUUID(), id2 = UUID.randomUUID();

        Customer customer1 = new Customer(id1, "Abel", "123456789"), customer2 = new Customer(id2, "Edgar", "123456789");

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer1);
        CustomerRegistrationService underTest = new CustomerRegistrationService(request, customerRepository);

        customerRepository.save(customer2);

        //When
        try {
            underTest.registerNewCustomer(request);
        }
        catch(Exception e) {
            System.out.println("We have an Exception");
            exception = true;
        }

        //Then
        assertThat(exception).isEqualTo(true);
    }
}