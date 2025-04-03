package kr.hhplus.be.server.token.dto;

public class TokenDomainRequestBodyDTO {
    public record TokenCreateRequestBodyDTO(
        long date_id
    ) {}

}
