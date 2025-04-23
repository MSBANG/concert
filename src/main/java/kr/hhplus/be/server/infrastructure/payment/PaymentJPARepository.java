package kr.hhplus.be.server.infrastructure.payment;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PaymentJPARepository extends JpaRepository<Payment, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Payment findByUserId(long userId);
}
