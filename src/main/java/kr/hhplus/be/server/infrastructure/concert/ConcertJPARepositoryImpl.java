package kr.hhplus.be.server.infrastructure.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
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
    public List<Concert> getAllConcerts() {
        return List.of();
    }

    @Override
    public List<ConcertSchedule> getAllConcertSchedules(long concertId) {
        return List.of();
    }
}
