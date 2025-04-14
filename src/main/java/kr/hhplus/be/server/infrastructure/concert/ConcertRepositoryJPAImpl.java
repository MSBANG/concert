package kr.hhplus.be.server.infrastructure.concert;


import kr.hhplus.be.server.domain.concert.domain.*;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertRepositoryJPAImpl implements ConcertRepository {

    @Override
    public List<Concert> getAllConcert() {
        return List.of(new Concert[0]);
    }

    @Override
    public ConcertWithDate getConcertWithDate(long concertId) {
        return null;
    }

    @Override
    public ConcertDateWithSeat getConcertDateWithSeat(long dateId) {
        return null;
    }

    @Override
    public boolean getSeatIsAvail(long seatId) {
        return false;
    }

    @Override
    public void saveSeatReservation(ReserveSeat reserveSeat) {

    }

    @Override
    public void updateSeatIsAvail(long seatId, boolean isAvail) {

    }

    @Override
    public ConcertSeat getConcertSeatById(long seatId) {
        return null;
    }

    @Override
    public List<Reservation> getAllReservations(long userId) {
        return List.of();
    }

    @Override
    public Reservation getReservationById(long reservationId) {
        return null;
    }

    @Override
    public void updateReservationStatus(long reservationId, long statusEnum) {

    }
}
