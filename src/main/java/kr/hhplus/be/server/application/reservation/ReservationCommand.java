package kr.hhplus.be.server.application.reservation;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationCommand {
    // 예약 기능을 사용하려 아래의 데이터를 보낸다
    // "누가" "어느날의" "어느 좌석을" 예약하는지
    private final long seatId;
    private final String queueToken;

    @Builder
    private ReservationCommand(long seatId, String queueToken) {
        this.seatId = seatId;
        this.queueToken = queueToken;
    }

    public static ReservationCommand of(long seatId, String queueToken) {
        return ReservationCommand.builder()
                .seatId(seatId)
                .queueToken(queueToken)
                .build();
    }
}
