package kr.hhplus.be.server.domain.concert;

// 콘서트 최상위 Table interface
public interface ConcertRepository {
    public Concert[] getAllConcert();
    public ConcertWithDate getConcertWithDate(long concertId);
    public ConcertDateWithSeat getConcertWithSeat(long dateId);
}
