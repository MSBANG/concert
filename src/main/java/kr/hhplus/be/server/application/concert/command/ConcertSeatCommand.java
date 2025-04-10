package kr.hhplus.be.server.application.concert.command;

import kr.hhplus.be.server.interfaces.api.common.APIException;

public record ConcertSeatCommand(
        long seatId,
        long dateId,
        boolean isAvail,
        long price
) {
    public ConcertSeatCommand {
        // 콘서트 가격은 0 이하일 수 없음
        if (price < 0) {
            throw APIException.seatPriceInsufficient();
        }
    }
}
