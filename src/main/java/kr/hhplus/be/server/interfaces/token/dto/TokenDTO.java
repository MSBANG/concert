package kr.hhplus.be.server.interfaces.token.dto;

public record TokenDTO(
        String token
) {
    public static TokenDTO getDefault() {return new TokenDTO("sample_jwt_token");};
}
