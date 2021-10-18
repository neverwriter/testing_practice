package com.amigoscode.testing.payment;

public class SmsPaymentSend {


    private boolean isSmsSent;

    public SmsPaymentSend(boolean isSmsSent) {
        this.isSmsSent = isSmsSent;
    }

    public boolean isSmsSent() {
        return isSmsSent;
    }
}
