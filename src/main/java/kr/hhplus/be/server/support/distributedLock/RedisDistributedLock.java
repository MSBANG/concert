package kr.hhplus.be.server.support.distributedLock;

import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.application.reservation.ReservationCommand;
import kr.hhplus.be.server.support.APIException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Order(-1)
@Component
@RequiredArgsConstructor
public class RedisDistributedLock implements DistributedLockAspect {
    private final StringRedisTemplate redisTemplate;
    private static final ThreadLocal<String> lockKeyHolder = new ThreadLocal<>();

    @Override
    @After("@annotation(kr.hhplus.be.server.support.distributedLock.DistributedLock)")
    public void release(JoinPoint joinPoint) {
        String redisKey = lockKeyHolder.get();
        if (redisKey != null) {
            redisTemplate.delete(redisKey);
            lockKeyHolder.remove();
        }
    }

    @Override
    @Before("@annotation(kr.hhplus.be.server.support.distributedLock.DistributedLock) && args(command)")
    public void acquire(PaymentCommand.Pay command) throws InterruptedException {
        String redisKey = "Lock:paymentUserId:%s".formatted(command.getUserId());
        if (!(this.setLockKey(redisKey))) {
            throw APIException.distributedLockException();
        }
    }

    @Override
    @Before("@annotation(kr.hhplus.be.server.support.distributedLock.DistributedLock) && args(command)")
    public void acquire(PaymentCommand.Charge command) throws InterruptedException {
        String redisKey = "Lock:paymentUserId:%s".formatted(command.getUserId());
        if (!(this.setLockKey(redisKey))) {
            throw APIException.distributedLockException();
        }
    }

    @Override
    @Before("@annotation(kr.hhplus.be.server.support.distributedLock.DistributedLock) && args(command)")
    public void acquire(ReservationCommand command) throws InterruptedException {
        String redisKey = "Lock:seatId:%s".formatted(command.getSeatId());
        if (!(this.setLockKey(redisKey))) {
            throw APIException.distributedLockException();
        }
    }

    // spin Lock 구현
    private boolean setLockKey(String redisKey) throws InterruptedException {
        for (int i=0; i<3; i++) {
            Boolean res = redisTemplate.opsForValue().setIfAbsent(redisKey, "1", Duration.ofSeconds(5));
            if (Boolean.FALSE.equals(res)) {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            } else {
                lockKeyHolder.set(redisKey);
                return true;
            }
        }
        return false;
    }
}
