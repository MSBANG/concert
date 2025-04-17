package kr.hhplus.be.server.interfaces.api.concert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.common.APIResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Tag(name="Concert", description="Concert Domain API")
public interface ConcertControllerDocs {
    @Operation(summary = "콘서트 목록 조회", description="콘서트 목록 조회")
    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse concertList();

    @Operation(summary = "콘서트 날짜조회", description="콘서트 date_id 를 기반으로 모든 좌석을 조회합니다.")
    @GetMapping(value="/{concertId}/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse concertSchedules(
            @PathVariable("concertId") long dateId
    );
}