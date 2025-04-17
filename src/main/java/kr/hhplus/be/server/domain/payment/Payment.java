package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.BaseEntity;
import kr.hhplus.be.server.support.APIException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    private long userId;

    private long balance;

    @Builder
    private Payment(long paymentId, long userId, long balance) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.balance = balance;
    }

    public static Payment create(long paymentId, long userId, long balance) {
        return Payment.builder()
                .paymentId(paymentId)
                .userId(userId)
                .balance(balance)
                .build();
    }

    public void use(long amount) {
        validateBalance(this.balance - amount);
        this.balance -= amount;
    }

    public void charge(long amount) {
        validateAmount(amount);
        this.balance += amount;
    }

    private void validateAmount(long amount) {
        if (amount <= 0) {
            throw APIException.insufficientAmount();
        }
    }

    private void validateBalance(long balance) {
        if (balance < 0) {
            throw APIException.insufficientBalance();
        }
    }
}
