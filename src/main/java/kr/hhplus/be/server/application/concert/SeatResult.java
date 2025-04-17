package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.ConcertSeat;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SeatResult {
    private final long seatId;
    private final int seatNum;
    private final boolean isAvail;
    private final long price;

    @Builder
    private SeatResult(long seatId, int seatNum, boolean isAvail, long price) {
        this.seatId = seatId;
        this.seatNum = seatNum;
        this.isAvail = isAvail;
        this.price = price;
    }

    public static SeatResult from(ConcertSeat seat) {
        return SeatResult.builder()
                .seatId(seat.getSeatId())
                .seatNum(seat.getSeatNum())
                .isAvail(seat.isAvail())
                .price(seat.getPrice())
                .build();
    }
}
