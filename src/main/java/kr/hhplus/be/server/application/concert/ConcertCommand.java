package kr.hhplus.be.server.application.concert;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(force = true)
@ToString
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
