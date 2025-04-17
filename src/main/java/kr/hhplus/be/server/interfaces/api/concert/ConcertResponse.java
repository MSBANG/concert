package kr.hhplus.be.server.interfaces.api.concert;

import kr.hhplus.be.server.application.concert.ConcertResult;

public record ConcertResponse(
        long concertId,
        String name
) {
    public static ConcertResponse from(ConcertResult concertResult) {
        return new ConcertResponse(
                concertResult.getConcertId(),
                concertResult.getName()
        );
    }
}
