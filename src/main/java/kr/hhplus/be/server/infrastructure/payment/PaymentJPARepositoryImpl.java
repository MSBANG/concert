package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentJPARepositoryImpl implements PaymentRepository {
    @Override
    public void updateBalance(long userId, long balance) {

    }

    @Override
    public Payment getPaymentByUserId(long userId) {
        return null;
    }

}
