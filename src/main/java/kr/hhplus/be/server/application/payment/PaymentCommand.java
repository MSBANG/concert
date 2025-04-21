package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.support.APIException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class PaymentCommand {
    @Getter
    public static class Pay {
        private final long userId;
        private final long reservationId;

        @Builder
        private Pay(long userId, long reservationId) {
            this.userId = userId;
            this.reservationId = reservationId;
        }

        public static Pay of(long userId, long reservationId) {
            return Pay.builder()
                    .userId(userId)
                    .reservationId(reservationId)
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Get {
        private final long userId;

        public static Get of(long userId){
            return new Get(userId);
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Charge {
        private final long userId;
        private final long amount;

        public static Charge of(long userId, long amount) {
            validateAmount(amount);
            return new Charge(userId, amount);
        }

        private static void validateAmount(long amount) {
           if (amount <= 0) {
               throw APIException.insufficientAmount();
           }
        }
    }
}
