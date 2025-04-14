package kr.hhplus.be.server.domain.payment;

public interface PaymentRepository {
    Payment getBalance(long userId);
    void updateBalance(Payment payment);
}
