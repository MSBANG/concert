package kr.hhplus.be.server.interfaces.api.common;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {
    // 성공
    OK(200, "OPERATION_SUCCESS"),

    //조회 실패
    FAILURE(400, "OPERATION_FAILURE"),
    PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
    CONCERT_NOT_FOUND(404, "CONCERT_NOT_FOUND"),
    SEAT_NOT_FOUND(404, "SEAT_NOT_FOUND"),
    SCHEDULE_NOT_FOUND(404, "SCHEDULE_NOT_FOUND"),

    //생성 실패
    START_DATE_AFTER_END_DATE(400, "START_DATE_AFTER_END_DATE"),
    SEAT_PRICE_INSUFFICIENT(400, "SEAT_PRICE_INSUFFICIENT"),

    //서버 Exception
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    //결제 Exception
    INSUFFICIENT_BALANCE(400, "INSUFFICIENT_BALANCE"),
    INSUFFICIENT_AMOUNT(400, "INSUFFICIENT_AMOUNT"),
    EXPIRED_RESERVATION(400, "EXPIRED_RESERVATION"),
    ALREADY_PAID_RESERVATION(400, "ALREADY_PAID_RESERVATION"),
    RESERVATION_NOT_FOUND(404, "RESERVATION_NOT_FOUND"),

    //예약 Exception
    SEAT_ALREADY_RESERVED(400, "SEAT_ALREADY_RESERVED"),
    ALL_SEATS_RESERVED(400, "ALL_SEATS_RESERVED");

    private final int statusCode;
    private final String message;

    ResponseCodeEnum(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
