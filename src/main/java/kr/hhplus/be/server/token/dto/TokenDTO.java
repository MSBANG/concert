package kr.hhplus.be.server.token.dto;

public record TokenDTO(
        String token
) {
    public static TokenDTO getDefault() {return new TokenDTO("sample_jwt_token");};
}
