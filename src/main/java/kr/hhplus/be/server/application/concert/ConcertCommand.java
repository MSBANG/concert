package kr.hhplus.be.server.application.concert;

import lombok.Builder;
import lombok.Getter;


@Getter
public class ConcertCommand {
    private final long concertId;

    @Builder
    private ConcertCommand(long concertId) {
        this.concertId = concertId;
    }

    public static ConcertCommand of (long concertId) {
        return ConcertCommand.builder()
                .concertId(concertId)
                .build();
    }
}
