package kr.hhplus.be.server.application.concert.command;

public record ReserveSeatCommand(
        long userId,
        long seatId
) {
}
