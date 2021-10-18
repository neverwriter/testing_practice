package com.amigoscode.testing.payment;

import java.math.BigDecimal;

public interface CardPaymentCharger {

    CardPaymentCharge chargeCard(
            String cardSours,
            BigDecimal amount,
            Currency currency,
            String description
    );
}
