package kr.hhplus.be.server.interfaces.queue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.token.dto.TokenDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Queue", description = "Queue Domain API")
public interface QueueController {
    @Operation(summary = "대기열 조회 요청", description = "대기열 조회를 요청합니다. Header 의 대기열 토큰을 사용하며, 순서에 도달한 경우 queue 상태를 예약 가능상태로 업데이트 하고, 업데이트된 토큰을 응답합니다.")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진입 순서 도달"),
    })
    TokenDTO CreateToken(
            @RequestHeader("X-queue-token") String queue_token
    );
}
