package kr.hhplus.be.server.application.concert.command;

public record ConcertDateWithSeatCommand(
    long dateId,
    ConcertSeatCommand[] concertSeats
) {
}
