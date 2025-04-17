package kr.hhplus.be.server.application.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import lombok.Builder;

public class ReservationResult {
    private final long reservationId;
    private final long userId;
    private final long seatId;
    private final long concertId;

    @Builder
    private ReservationResult(long reservationId, long userId, long seatId, long concertId) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.seatId = seatId;
        this.concertId = concertId;
    }

    public static ReservationResult from(Reservation reservation) {
        return ReservationResult.builder()
                .reservationId(reservation.getReservationId())
                .seatId(reservation.getSeat().getSeatId())
                .concertId(reservation.getConcert().getConcertId())
                .build();
    }
}
