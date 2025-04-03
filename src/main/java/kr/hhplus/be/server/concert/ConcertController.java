package kr.hhplus.be.server.concert;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.concert.dto.ConcertDTO;
import kr.hhplus.be.server.concert.dto.ConcertDateWithSeatDTO;
import kr.hhplus.be.server.concert.dto.ConcertSeatDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/concert")
@Tag(name="Concert", description="Concert Domain API")
public class ConcertController {
    @Operation(summary = "콘서트 목록 조회", description="콘서트 목록 조회")
    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConcertDTO> ConcertList() {
        return Arrays.asList(
                ConcertDTO.getDefault(),
                ConcertDTO.getDefault()
        );
    }

    @Operation(summary = "콘서트 좌석조회", description="콘서트 date_id 를 기반으로 모든 좌석을 조회합니다.")
    @GetMapping(value="/seats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConcertDateWithSeatDTO ConcertSeat(
            @RequestHeader("X-queue-token") String queue_token,
            @RequestParam("date_id") long dateId
    ) {
        return ConcertDateWithSeatDTO.getDefault();
    }

    @Operation(summary = "콘서트 좌석 예약 요청", description="seat_id 에 해당하는 좌석 예약을 요청합니다")
    @PostMapping(value="/seats/{seat_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConcertSeatDTO reserveSeat(
            @RequestHeader("X-queue-token") String queue_token,
            @PathVariable("seat_id") long seatId
    ) {
        return ConcertSeatDTO.getDefault();
    }
}
