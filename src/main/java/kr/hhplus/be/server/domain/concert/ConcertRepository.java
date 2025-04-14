package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.concert.domain.*;

import java.util.List;

// 콘서트 최상위 Table interface
public interface ConcertRepository {
    //콘서트 조회
    List<Concert> getAllConcert();
    ConcertWithDate getConcertWithDate(long concertId);
    ConcertDateWithSeat getConcertDateWithSeat(long dateId);
    ConcertSeat getConcertSeatById(long seatId);


    //좌석 상태 확인
    boolean getSeatIsAvail(long seatId);

    //좌석 예약
    void saveSeatReservation(ReserveSeat reserveSeat);
    void updateSeatIsAvail(long seatId, boolean isAvail);

    // 예약 내역 조회
    List<Reservation> getAllReservations(long userId);
    Reservation getReservationById(long reservationId);
    
    // 예약 상태 변경
    void updateReservationStatus(long reservationId, long statusEnum);
}
