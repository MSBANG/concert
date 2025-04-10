package kr.hhplus.be.server.interfaces.api.common;

public enum ResponseCodeEnum {
    // 성공
    OK(200, "OPERATION_SUCCESS"),

    //실패
    FAILURE(400, "OPERATION_FAILURE"),
    PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
    SEAT_NOT_FOUND(404, "SEAT_NOT_FOUND"),
    DATE_NOT_FOUND(404, "DATE_NOT_FOUND"),

    //서버 에러
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

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
