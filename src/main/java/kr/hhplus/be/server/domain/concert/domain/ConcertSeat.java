package kr.hhplus.be.server.domain.concert.domain;

import kr.hhplus.be.server.interfaces.api.common.APIException;

public record ConcertSeat(
        long seatId,
        long dateId,
        boolean isAvail,
        long price
) {
    public ConcertSeat{
        // 콘서트 가격은 0 이하일 수 없음
        if (price < 0) {
            throw APIException.seatPriceInsufficient();
        }
    }
}
