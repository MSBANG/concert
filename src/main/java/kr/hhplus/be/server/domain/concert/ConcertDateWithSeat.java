package kr.hhplus.be.server.domain.concert;

public record ConcertDateWithSeat(
    long dateId,
    ConcertSeat[] concertSeats
) {
}
