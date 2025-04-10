package kr.hhplus.be.server.interfaces.api.common;

import io.micrometer.common.lang.Nullable;

public class APIException extends RuntimeException {
    private final APIResponse apiResponse;

    public APIException(APIResponse apiResponse) {
        super(apiResponse.message());
        this.apiResponse = apiResponse;
    }

    public APIResponse getApiResponse() {
        return apiResponse;
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
    public static APIException expiredReservation() { return new APIException(APIResponse.expiredReservation()); }

}