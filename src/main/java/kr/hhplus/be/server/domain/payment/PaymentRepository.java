package kr.hhplus.be.server.domain.payment;

public interface PaymentRepository {
    Payment getPaymentByUserId(long userId);
}
