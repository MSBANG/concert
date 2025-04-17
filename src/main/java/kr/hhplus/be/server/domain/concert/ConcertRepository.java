package kr.hhplus.be.server.domain.concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> getConcertById(long concertId);
    Optional<ConcertSchedule> getScheduleById(long scheduleId);
    Optional<ConcertSeat> getSeatById(long seatId);
    List<Concert> getAllConcerts();
    List<ConcertSchedule> getAllConcertSchedules(long concertId);
    List<ConcertSeat> getAllSeats(long scheduleId);
}
