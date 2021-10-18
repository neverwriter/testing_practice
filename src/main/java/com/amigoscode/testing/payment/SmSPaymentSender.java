package com.amigoscode.testing.payment;

import java.math.BigDecimal;

public interface SmSPaymentSender {

    SmsPaymentSend smsPaymentSend (
            String phoneNumber,
            BigDecimal amount,
            Currency currency,
            String description
    );

}
