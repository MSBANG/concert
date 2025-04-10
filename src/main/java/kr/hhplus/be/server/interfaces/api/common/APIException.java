package kr.hhplus.be.server.interfaces.api.common;

public class APIException extends RuntimeException {
    private final APIResponse apiResponse;

    public APIException(APIResponse apiResponse) {
        super(apiResponse.message());
        this.apiResponse = apiResponse;
    }

    public APIResponse getApiResponse() {
        return apiResponse;
    }
}