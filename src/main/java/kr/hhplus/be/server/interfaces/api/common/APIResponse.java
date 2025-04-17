package kr.hhplus.be.server.interfaces.api.common;

import io.micrometer.common.lang.Nullable;

/** 성공
 * {
 *      "success": true,
 *      "code": 2000(enum 지정된 코드?)
 *      "message": "OPERATION_SUCCESS"
 *      "data": {
 *          "type": "list"(or "object")
 *          "item": {}(or [])
 *      }(or null)
 * }
 * 실패
 * {
 *      "success": false,
 *      "code": 4000
 *      "message": "OPERATION_FAILURE"
 *      "data": {} (or null)
 * }
 * **/
// 응답할 data 가 없는 경우도 대응 되어야함

public record APIResponse(
    boolean success,
    int statusCode,
    String message,
    @Nullable ResponseItem<?> data
) {
    //성공

    public static <T> APIResponse ok(@Nullable ResponseItem<T> data) {
        return new APIResponse(true, ResponseCodeEnum.OK.getStatusCode(), ResponseCodeEnum.OK.getMessage(), data);
    }

    //실패

    public static <T> APIResponse failure(@Nullable ResponseItem<T> data) {
        return new APIResponse(false, ResponseCodeEnum.FAILURE.getStatusCode(), ResponseCodeEnum.FAILURE.getMessage(), data);
    }

    public static APIResponse pageNotFound() {
        return new APIResponse(false, ResponseCodeEnum.PAGE_NOT_FOUND.getStatusCode(), ResponseCodeEnum.PAGE_NOT_FOUND.getMessage(), null);
    }

    public static APIResponse startDateAfterEndDate() {
        return new APIResponse(false, ResponseCodeEnum.START_DATE_AFTER_END_DATE.getStatusCode(), ResponseCodeEnum.START_DATE_AFTER_END_DATE.getMessage(), null);
    }

    public static APIResponse seatPriceInsufficient() {
        return new APIResponse(false, ResponseCodeEnum.SEAT_PRICE_INSUFFICIENT.getStatusCode(), ResponseCodeEnum.SEAT_PRICE_INSUFFICIENT.getMessage(), null);
    }

    public static APIResponse seatAlreadyReserved() {
        return new APIResponse(false, ResponseCodeEnum.SEAT_ALREADY_RESERVED.getStatusCode(), ResponseCodeEnum.SEAT_ALREADY_RESERVED.getMessage(), null);
    }

    public static APIResponse insufficientBalance() {
        return new APIResponse(false, ResponseCodeEnum.INSUFFICIENT_BALANCE.getStatusCode(), ResponseCodeEnum.INSUFFICIENT_BALANCE.getMessage(), null);
    }

    public static APIResponse insufficientAmount() {
        return new APIResponse(false, ResponseCodeEnum.INSUFFICIENT_AMOUNT.getStatusCode(), ResponseCodeEnum.INSUFFICIENT_AMOUNT.getMessage(), null);
    }

    public static APIResponse expiredReservation() {
        return new APIResponse(false, ResponseCodeEnum.EXPIRED_RESERVATION.getStatusCode(), ResponseCodeEnum.EXPIRED_RESERVATION.getMessage(), null);
    }

    public static APIResponse alreadyPaidReservation() {
        return new APIResponse(false, ResponseCodeEnum.ALREADY_PAID_RESERVATION.getStatusCode(), ResponseCodeEnum.ALREADY_PAID_RESERVATION.getMessage(), null);
    }

    public static APIResponse concertNotFound() {
        return new APIResponse(false, ResponseCodeEnum.CONCERT_NOT_FOUND.getStatusCode(), ResponseCodeEnum.CONCERT_NOT_FOUND.getMessage(), null);
    }

    public static APIResponse scheduleNotFound() {
        return new APIResponse(false, ResponseCodeEnum.SCHEDULE_NOT_FOUND.getStatusCode(), ResponseCodeEnum.SCHEDULE_NOT_FOUND.getMessage(), null);
    }

    public static APIResponse seatNotFound() {
        return new APIResponse(false, ResponseCodeEnum.SEAT_NOT_FOUND.getStatusCode(), ResponseCodeEnum.SEAT_NOT_FOUND.getMessage(), null);
    }
}
