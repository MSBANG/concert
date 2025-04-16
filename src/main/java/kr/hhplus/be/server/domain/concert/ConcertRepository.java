package kr.hhplus.be.server.domain.concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> getConcertById(long concertId);
    List<Concert> getAllConcerts();
    List<ConcertSchedule> getAllConcertSchedules(long concertId);
}

