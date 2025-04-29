package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import kr.hhplus.be.server.infrastructure.payment.PaymentJPARepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PaymentConcurrencyTest {
    @Autowired
    private PaymentJPARepository paymentJPARepository;

    @Autowired
    private PlatformTransactionManager pm;

    @Test
    void 동일_Payment에_대한_동시_수정은_모두_순차적으로_적용된다() throws InterruptedException {
        // given
        long userId = 1L;
        long initAmount = 10000L;
        Payment payment = Payment.create(userId, initAmount);
        paymentJPARepository.save(payment);

        long chargeAmount = 1000L;
        long useAmount = 1000L;

        CountDownLatch latch = new CountDownLatch(2);
        Runnable useTask = () -> {
            TransactionStatus status = pm.getTransaction(new DefaultTransactionDefinition());
            Payment payment1 = paymentJPARepository.findByUserId(userId);
            payment1.use(useAmount);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pm.commit(status);
            latch.countDown();
        };

        Runnable chargeTask = () -> {
            TransactionStatus status = pm.getTransaction(new DefaultTransactionDefinition());
            Payment payment2 = paymentJPARepository.findByUserId(userId);
            payment2.charge(chargeAmount);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pm.commit(status);
            latch.countDown();
        };

        new Thread(useTask).start();
        new Thread(chargeTask).start();

        latch.await();

        TransactionStatus status = pm.getTransaction(new DefaultTransactionDefinition());
        Payment result = paymentJPARepository.findByUserId(userId);
        pm.commit(status);
        System.out.println("최종 사용자 포인트: " + result.getBalance());
        assertThat(result.getBalance())
                .as("모든 사용, 충전 메서드가 순차적으로 실행된다")
                .isEqualTo(initAmount + chargeAmount - useAmount);
    }
}
