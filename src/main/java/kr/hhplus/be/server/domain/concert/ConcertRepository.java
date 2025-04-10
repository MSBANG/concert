package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.concert.domain.*;

import java.util.List;

// 콘서트 최상위 Table interface
public interface ConcertRepository {
    List<Concert> getAllConcert();
    ConcertWithDate getConcertWithDate(long concertId);
    ConcertDateWithSeat getConcertDateWithSeat(long dateId);
    boolean getSeatIsAvail(long seatId);
    void saveSeatReservation(ReserveSeat reserveSeat);
    void updateSeatIsAvail(long seatId, boolean isAvail);
    ConcertSeat getConcertSeatById(long seatId);
}
