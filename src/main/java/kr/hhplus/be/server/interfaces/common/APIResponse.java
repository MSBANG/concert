package kr.hhplus.be.server.interfaces.common;

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
}
