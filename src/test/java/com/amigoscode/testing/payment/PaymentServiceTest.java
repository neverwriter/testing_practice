package com.amigoscode.testing.payment;


import com.amigoscode.testing.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class PaymentServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;

    private PaymentService underTest;

    private PaymentRequest paymentRequest;

    @Mock
    private CardPaymentCharger cardPaymentCharger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new PaymentService(customerRepository, paymentRequest, cardPaymentCharger, paymentRepository);
    }

    @Test
    void itShouldChargeCard() {
        //Given a customer and a payment
        UUID customerID = UUID.randomUUID();
        long paymentId = 1L;

        Payment payment = new Payment(
                paymentId,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.USD,
                "card123",
                "Donation");

        // ... a request and payment charge

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(true);

        given(customerRepository.existsById(customerID)).willReturn(true);

        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescripton())).willReturn(cardPaymentCharge);

        //When

        underTest.chargeCard(customerID, paymentRequest);

        //Then

        then(paymentRepository).should().save(paymentArgumentCaptor.capture());
    }

    @Test
    void itShouldThrowExceptionWhenCustomerByIdNotExist() {
        //Given a customer and a payment
        UUID customerID = UUID.randomUUID();
        long paymentId = 1L;

        Payment payment = new Payment(
                paymentId,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.USD,
                "card123",
                "Donation");

        // ... a request

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        given(customerRepository.existsById(customerID)).willReturn(false);

        //When
        //Then
        assertThatThrownBy(() -> underTest.chargeCard(customerID, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(String.format("Customer with ID [%s] does not exist", customerID));

        //Finally
        //then(paymentRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowExceptionWhenCurrencyNotSupported() {
        //Given a customer and a payment
        UUID customerID = UUID.randomUUID();
        long paymentId = 1L;



        Payment payment = new Payment(
                paymentId,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.EUR,
                "card123",
                "Donation");

        // ... a request

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        given(customerRepository.existsById(customerID)).willReturn(true);
        //When
        //Then
        assertThatThrownBy(() -> underTest.chargeCard(customerID, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Currency [%s] not supported", paymentRequest.getPayment().getCurrency()));

        //Finally
        //then(paymentRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowExceptionWhenCardNotCharged() {
        //Given a customer and a payment
        UUID customerID = UUID.randomUUID();
        long paymentId = 1L;

        Payment payment = new Payment(
                paymentId,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.USD,
                "card123",
                "Donation");

        // ... a request and payment charge

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(false);

        given(customerRepository.existsById(customerID)).willReturn(true);

        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescripton())).willReturn(cardPaymentCharge);

        //When
        //Then
        assertThatThrownBy(() -> underTest.chargeCard(customerID, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Card [%s] not debited", paymentRequest.getPayment().getSource()));

        //Finally
        //then(paymentRepository).should(never()).save(any());
    }
}