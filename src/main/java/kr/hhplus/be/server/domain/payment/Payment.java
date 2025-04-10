package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.interfaces.api.common.APIException;

public class Payment{
    private final long userId;
    private long balance;

    public Payment(long userId, long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public void charge(long amount) {
        this.balance += amount;
    }

    public void use(long amount) {
        if (this.balance - amount < 0) {
            throw APIException.insufficientBalance();
        }
        this.balance -= amount;
    }
}
