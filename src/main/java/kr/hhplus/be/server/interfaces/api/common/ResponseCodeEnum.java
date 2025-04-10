package kr.hhplus.be.server.interfaces.api.common;

public enum ResponseCodeEnum {
    // 성공
    OK(200, "OPERATION_SUCCESS"),

    //조회 실패
    FAILURE(400, "OPERATION_FAILURE"),
    PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
    SEAT_NOT_FOUND(404, "SEAT_NOT_FOUND"),
    DATE_NOT_FOUND(404, "DATE_NOT_FOUND"),

    //생성 실패
    START_DATE_AFTER_END_DATE(400, "START_DATE_AFTER_END_DATE"),
    SEAT_PRICE_INSUFFICIENT(400, "SEAT_PRICE_INSUFFICIENT"),

    //서버 Exception
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    //결제 Exception
    INSUFFICIENT_BALANCE(400, "INSUFFICIENT_BALANCE"),
    EXPIRED_RESERVATION(400, "EXPIRED_RESERVATION"),

    //예약 Exception
    SEAT_ALREADY_RESERVED(400, "SEAT_ALREADY_RESERVED");

    private final int statusCode;
    private final String message;

    ResponseCodeEnum(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
