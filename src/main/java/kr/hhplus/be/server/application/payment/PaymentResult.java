package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentResult {
    private final long userId;
    private final long balance;

    @Builder
    private PaymentResult(long userId, long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public static PaymentResult from(Payment payment) {
        return PaymentResult.builder()
                .userId(payment.getUserId())
                .balance(payment.getBalance())
                .build();
    }
}
