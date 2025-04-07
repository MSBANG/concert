package kr.hhplus.be.server.interfaces.token.dto;

public class TokenDomainRequestBodyDTO {
    public record TokenCreateRequestBodyDTO(
        long date_id
    ) {}

}
