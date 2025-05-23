package kr.hhplus.be.server.support;

import io.micrometer.common.lang.Nullable;
import kr.hhplus.be.server.interfaces.api.common.APIResponse;
import kr.hhplus.be.server.interfaces.api.common.ResponseItem;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private final APIResponse apiResponse;

    public APIException(APIResponse apiResponse) {
        super(apiResponse.message());
        this.apiResponse = apiResponse;
    }

    public static APIException pageNotFound() {
        return new APIException(APIResponse.pageNotFound());
    }

    public static APIException startDateAfterEndDate() {
        return new APIException(APIResponse.startDateAfterEndDate());
    }

    public static APIException failure(@Nullable ResponseItem<?> data){
        return new APIException(APIResponse.failure(data));
    }

    public static APIException seatPriceInsufficient() {
        return new APIException(APIResponse.seatPriceInsufficient());
    }
    public static APIException seatAlreadyReserved() {
        return new APIException(APIResponse.seatAlreadyReserved());
    }

    public static APIException insufficientBalance() { return new APIException(APIResponse.insufficientBalance()); }
    public static APIException insufficientAmount() { return new APIException(APIResponse.insufficientAmount()); }
    public static APIException expiredReservation() { return new APIException(APIResponse.expiredReservation()); }
    public static APIException alreadyPaidReservation() { return new APIException(APIResponse.alreadyPaidReservation()); }
    public static APIException concertNotFound() { return new APIException(APIResponse.concertNotFound()); }
    public static APIException scheduleNotFound() { return new APIException(APIResponse.scheduleNotFound()); }
    public static APIException seatNotFound() { return new APIException(APIResponse.seatNotFound()); }
    public static APIException reservationNotFound() { return new APIException(APIResponse.reservationNotFound()); }
    public static APIException allSeatReserved() { return new APIException(APIResponse.allSeatReserved()); }
    public static APIException invalidReservationExecuting() { return new APIException(APIResponse.invalidReservationExecuting()); }
    public static APIException queueTokenNotReady() { return new APIException(APIResponse.queueTokenNotReady()); }
    public static APIException queueExpiredOrInvalid() { return new APIException(APIResponse.queueExpiredOrInvalid()); }
    public static APIException optimisticLockException() { return new APIException(APIResponse.optimisticLockException()); }
    public static APIException distributedLockException() { return new APIException(APIResponse.distributedLockException()); }
    public static APIException noSeatRemain() { return new APIException(APIResponse.noSeatRemain()); }
}