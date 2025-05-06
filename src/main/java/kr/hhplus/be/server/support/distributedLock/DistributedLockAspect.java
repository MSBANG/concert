package kr.hhplus.be.server.support.distributedLock;

import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.application.reservation.ReservationCommand;
import org.aspectj.lang.JoinPoint;

public interface DistributedLockAspect {
    void release(JoinPoint joinPoint);
    void acquire(PaymentCommand.Pay command) throws InterruptedException;
    void acquire(PaymentCommand.Charge command) throws InterruptedException;
    void acquire(ReservationCommand command) throws InterruptedException;
}