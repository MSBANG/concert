package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.interfaces.api.common.APIException;

public record PaymentCommand(
        long userId,
        long balance
) {
    public PaymentCommand{
        if (balance <= 0) {
            throw APIException.insufficientBalance();
        }
    }
}
