package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryJPAImpl implements PaymentRepository {
    @Override
    public Payment getBalance(long userId) {
        return null;
    }

    @Override
    public void updateBalance(Payment payment) {
    }
}
