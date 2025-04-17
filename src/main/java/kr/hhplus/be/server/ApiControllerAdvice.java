package kr.hhplus.be.server;

import kr.hhplus.be.server.support.APIException;
import kr.hhplus.be.server.interfaces.api.common.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> handleAPIException(APIException apiException){
        APIResponse apiResponse = apiException.getApiResponse();
        return ResponseEntity.status(apiResponse.statusCode()).body(apiResponse);
    }
}
