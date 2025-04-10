package kr.hhplus.be.server.interfaces.api.token.dto;

public record TokenDTO(
        String token
) {
    public static TokenDTO getDefault() {return new TokenDTO("sample_jwt_token");};
}
