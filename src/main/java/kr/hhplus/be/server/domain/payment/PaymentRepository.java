package kr.hhplus.be.server.domain.payment;

public interface PaymentRepository {
    void updateBalance(long userId, long balance);
    Payment getPaymentByUserId(long userId);
}
