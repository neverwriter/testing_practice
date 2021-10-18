package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class PaymentService {

    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.GBP, Currency.USD);

    private final CustomerRepository customerRepository;

    private final CardPaymentCharger cardPaymentCharger;
    private final PaymentRepository paymentRepository;
    private final SmSPaymentSender smSPaymentSender;

    @Autowired
    public PaymentService(
            CustomerRepository customerRepository,
            CardPaymentCharger cardPaymentCharger,
            PaymentRepository paymentRepository,
            SmSPaymentSender smSPaymentSender) {
        this.customerRepository = customerRepository;
        this.cardPaymentCharger = cardPaymentCharger;
        this.paymentRepository = paymentRepository;
        this.smSPaymentSender = smSPaymentSender;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest) {

        if (!customerRepository.existsById(customerId)) {
            throw new IllegalStateException(String.format(
                    "Customer with ID [%s] does not exist", customerId));
        }

        boolean isCurrencySupported = ACCEPTED_CURRENCIES.contains(
                paymentRequest.getPayment().getCurrency());

        if (!isCurrencySupported) {
            throw new IllegalStateException(String.format(
                    "Currency [%s] not supported", paymentRequest.getPayment().getCurrency()));
        }


        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescripton());

        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(String.format(
                    "Card [%s] not debited", paymentRequest.getPayment().getSource()));
        }
        paymentRequest.getPayment().setCustomerId(customerId);

        paymentRepository.save(paymentRequest.getPayment());

        // 6. TODO: send sms

//                    smSPaymentSender.smsPaymentSend(
//                            customerRepository.findById(customerId).get().getPhoneNumber(),
//                            paymentRequest.getPayment().getAmount(),
//                            paymentRequest.getPayment().getCurrency(),
//                            paymentRequest.getPayment().getDescripton());


    }
}
