package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final PaymentRequest paymentRequest;
    private final CardPaymentCharger cardPaymentCharger;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(CustomerRepository customerRepository, PaymentRequest paymentRequest, CardPaymentCharger cardPaymentCharger, PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.paymentRequest = paymentRequest;
        this.cardPaymentCharger = cardPaymentCharger;
        this.paymentRepository = paymentRepository;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest){

        if(customerRepository.existsById(customerId)){
            if(Stream.of(Currency.GBP, Currency.USD).anyMatch(e -> e.equals(paymentRequest.getPayment().getCurrency()))){
                CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                        paymentRequest.getPayment().getSource(),
                        paymentRequest.getPayment().getAmount(),
                        paymentRequest.getPayment().getCurrency(),
                        paymentRequest.getPayment().getDescripton());
                if(cardPaymentCharge.isCardDebited()){
                    paymentRepository.save(paymentRequest.getPayment());
                    return;
                }
                throw new IllegalStateException(String.format("Card [%s] not debited", paymentRequest.getPayment().getSource()));
            }
            throw new IllegalStateException(String.format("Currency [%s] not supported", paymentRequest.getPayment().getCurrency()));
        }
        throw new IllegalStateException(String.format("Customer with ID [%s] does not exist", customerId));

// 1. Does customer exist if not throw
        // 2. Do we support the currency if not throw
        // 3. Charge card
        // 4. If not debited throw
        // 5. Insert payment
        // 6. TODO: send sms

    }
}
