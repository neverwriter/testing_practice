package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;


    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }


    public void registerNewCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {

        String phoneNumber = request.getCustomer().getPhoneNumber();

        Optional<Customer> customerOptional = customerRepository
                .selectCustomerByPhoneNumber(phoneNumber);

        if (customerOptional.isPresent()) {
            if (customerOptional.get().getName()
                    .equals(request.getCustomer().getName())) {
                return;
            }
            throw new IllegalStateException(String.format("Phone number [%s] is taken", phoneNumber));
        }

        if(request.getCustomer().getId() == null){
            request.getCustomer().setId(UUID.randomUUID());
        }

        customerRepository.save(request.getCustomer());

        // 1. PhoneNumber is taken
        // 2. if taken lets check if belong to same customer
        // - 2.1 if yes return
        // - 2.2 thrown an exception
        // 3. Save customer
    }
}
