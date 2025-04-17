package kr.hhplus.be.server.application.reservation;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationCommand {
    // 예약 기능을 사용하려 아래의 데이터를 보낸다
    // "누가" "어느날의" "어느 좌석을" 예약하는지
    private final long reservationId;
    private final long userId;
    private final long seatId;
    private final long concertId;

    @Builder
    private ReservationCommand(long reservationId, long userId, long seatId, long concertId) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.seatId = seatId;
        this.concertId = concertId;
    }

    public static ReservationCommand of(long userId, long seatId, long concertId) {
        return ReservationCommand.builder()
                .userId(userId)
                .seatId(seatId)
                .concertId(concertId)
                .build();
    }

    public ReservationCommand fromReservationId(long reservationId) {
        return ReservationCommand.builder()
                .reservationId(reservationId)
                .build();
    }
}
