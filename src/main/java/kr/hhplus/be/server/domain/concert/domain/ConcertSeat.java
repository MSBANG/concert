package kr.hhplus.be.server.domain.concert.domain;

import kr.hhplus.be.server.interfaces.api.common.APIException;

public class ConcertSeat{
    long seatId;
    long dateId;
    boolean isAvail;
    long price;

    public ConcertSeat(long seatId, long dateId, boolean isAvail, long price) {
        this.seatId = seatId;
        this.dateId = dateId;
        this.isAvail = isAvail;
        this.price = price;
    }

    public void reserve() {
        if (!this.isAvail) {
            throw APIException.seatAlreadyReserved();
        }
        this.isAvail = false;
    }

    public long getSeatId() {
        return seatId;
    }

    public boolean getIsAvail() {
        return isAvail;
    }

    public long getPrice() {
        return price;
    }
}
