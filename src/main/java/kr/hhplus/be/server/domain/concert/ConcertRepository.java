package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.concert.domain.Concert;
import kr.hhplus.be.server.domain.concert.domain.ConcertDateWithSeat;
import kr.hhplus.be.server.domain.concert.domain.ConcertWithDate;

import java.util.List;

// 콘서트 최상위 Table interface
public interface ConcertRepository {
    public List<Concert> getAllConcert();
    public ConcertWithDate getConcertWithDate(long concertId);
    public ConcertDateWithSeat getConcertDateWithSeat(long dateId);
    public boolean getSeatIsAvail(long seatId);
}
