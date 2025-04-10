package kr.hhplus.be.server.domain.concert.domain;

import kr.hhplus.be.server.interfaces.api.common.APIException;

public class Reservation{
    long reservationId;
    long userId;
    long seatId;
    long concertId;
    long statusEnum;

    public void reserve() {
        if (this.statusEnum == -1) {
            throw APIException.expiredReservation();
        }
        this.statusEnum = 1;
    }

    public void expire() {
        this.statusEnum = -1;
    }

    public long getReservationId() {
        return reservationId;
    }

    public long getUserId() {
        return userId;
    }

    public long getSeatId() {
        return seatId;
    }

    public long getConcertId() {
        return concertId;
    }

    public long getStatusEnum() {
        return statusEnum;
    }
}

