package kr.hhplus.be.server.interfaces.api.token;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.token.dto.TokenDTO;
import kr.hhplus.be.server.interfaces.api.token.dto.TokenDomainRequestBodyDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Token", description = "Token Domain API")
public interface TokenController {
    @Operation(summary = "대기열 토큰 발급 요청", description = "콘서트 date_id로 DB를 조회하고, 대기열 데이터를 기반으로 토큰을 생성합니다. 대기가 불가능할 경우 에러를 응답합니다.")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기열 토큰 발급 성공"),
    })
    TokenDTO CreateToken(
            @RequestBody TokenDomainRequestBodyDTO.TokenCreateRequestBodyDTO request
    );
}
