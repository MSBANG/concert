package kr.hhplus.be.server.application.concert.command;

public record ReservationCommand(
        long reservationId,
        long userId,
        long seatId,
        long concertId,
        long statusEnum
) {

}
