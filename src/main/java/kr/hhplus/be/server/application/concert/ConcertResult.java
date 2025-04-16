package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ConcertResult {
    private final long concertId;
    private final String name;

    @Builder
    private ConcertResult(long concertId, String name) {
        this.concertId = concertId;
        this.name = name;
    }

    public static ConcertResult from(Concert concert) {

        return ConcertResult.builder()
                .concertId(concert.getConcertId())
                .name(concert.getName())
                .build();
    }


}
