package kr.hhplus.be.server.infrastructure.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConcertJPARepositoryImpl implements ConcertRepository {
    @Override
    public Optional<Concert> getConcertById(long concertId) {
        return Optional.empty();
    }

    @Override
    public Optional<ConcertSchedule> getScheduleById(long scheduleId) {
        return Optional.empty();
    }

    @Override
    public Optional<ConcertSeat> getSeatById(long seatId) {
        return Optional.empty();
    }

    @Override
    public void updateSeatIsAvail(long seatId, boolean isAvail) {

    }

    @Override
    public List<Concert> getAllConcerts() {
        return List.of();
    }

    @Override
    public List<ConcertSchedule> getAllConcertSchedules(long concertId) {
        return List.of();
    }

    @Override
    public List<ConcertSeat> getAllSeats(long scheduleId) {
        return List.of();
    }
}
