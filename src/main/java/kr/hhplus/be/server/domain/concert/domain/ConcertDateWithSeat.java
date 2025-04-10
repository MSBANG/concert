package kr.hhplus.be.server.domain.concert.domain;

public record ConcertDateWithSeat(
    long dateId,
    ConcertSeat[] concertSeats
) {
}
