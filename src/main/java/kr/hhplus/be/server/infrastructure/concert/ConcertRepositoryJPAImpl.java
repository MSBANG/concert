package kr.hhplus.be.server.infrastructure.concert;


import kr.hhplus.be.server.domain.concert.domain.Concert;
import kr.hhplus.be.server.domain.concert.domain.ConcertDateWithSeat;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.domain.ConcertWithDate;
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
}
