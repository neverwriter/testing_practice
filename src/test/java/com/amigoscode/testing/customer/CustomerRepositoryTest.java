package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        //Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "123456789";
        Customer customer = new Customer(id, "Abel", phoneNumber);

        //When
        underTest.save(customer);

        //Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualToComparingFieldByField(customer);
                });
    }

    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenCustomerNotExist() {
        //Given
        String phoneNumber = "1";

        //When
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        
        //Then
        assertThat(optionalCustomer).isNotPresent();

    }

    @Test
    public void itShouldSaveCustomer() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "123456789");

        //When
        underTest.save(customer);

        //Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualToComparingFieldByField(customer);
                });
    }

    @Test
    public void itShouldNotSaveCustomerWhenNameIsNull() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "123456789");

        //When
        //Then
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", null);

        //When
        //Then
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}