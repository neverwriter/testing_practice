package com.amigoscode.testing.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;


class CustomerRegistrationServiceTest {


    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldRegisterNewCustomer() {
        //Given a phone number and a customer
        String phoneNumber = "123456789";
        Customer customer = new Customer(UUID.randomUUID(), "Abel", phoneNumber);

        // ... a request

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        underTest.registerNewCustomer(request);

        //Then

        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);

    }

    @Test
    void itShouldRegisterNewCustomerWhenIdIsNull() {
        //Given a phone number and a customer
        String phoneNumber = "123456789";
        Customer customer = new Customer(null, "Abel", phoneNumber);

        // ... a request

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        underTest.registerNewCustomer(request);

        //Then

        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue)
                .isEqualToIgnoringGivenFields(customer, "id");
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();

    }

    @Test
    void itShouldNotRegisterNewCustomerWhetCustomerExists() {
        //Given a phone number, a customer
        String phoneNumber = "123456789";
        Customer customer = new Customer(UUID.randomUUID(), "Edgar", phoneNumber);

        // ... a request

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... an existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        //When
        underTest.registerNewCustomer(request);

        //Then

        then(customerRepository).should(never()).save(any());
//       then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
//       then(customerRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    void itShouldThrowAnException() {
        //Given a phone number, a customerToRegister and customerFromDB
        String phoneNumber = "123456789";
        Customer customerToRegister = new Customer(UUID.randomUUID(), "Abel", phoneNumber), customerFromDB = new Customer(UUID.randomUUID(), "Edgar", phoneNumber);

        // ... a request

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customerToRegister);

        // ... CustomerFromDB with phone number passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerFromDB));

        //When
        //Then
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Phone number [%s] is taken", phoneNumber));

        //Finally
        then(customerRepository).should(never()).save(any());

    }

}