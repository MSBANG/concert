package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdaptor implements PaymentRepository {
    private final PaymentJPARepository paymentJpaRepository;


    @Override
    public Payment getPaymentByUserId(long userId) {
        return paymentJpaRepository.findByUserId(userId);
    }

}
