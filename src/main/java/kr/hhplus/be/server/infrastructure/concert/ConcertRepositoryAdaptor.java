package kr.hhplus.be.server.infrastructure.concert;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.domain.concert.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryAdaptor implements ConcertRepository {
    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Optional<Concert> getConcertById(long concertId) {
        return concertJpaRepository.findById(concertId);
    }

    @Override
    public Optional<ConcertSchedule> getScheduleById(long scheduleId) {
        return concertScheduleJpaRepository.findById(scheduleId);
    }

    @Override
    public Optional<ConcertSeat> getSeatById(long seatId) {
        return concertSeatJpaRepository.findById(seatId);
    }

    @Override
    public List<Concert> getAllConcerts() {
        return concertJpaRepository.findAll();
    }

    @Override
    public List<ScheduleDTO> getAllConcertSchedules(long concertId) {
        return concertScheduleJpaRepository.findAllByConcert_ConcertId(concertId);
    }

    @Override
    public List<ConcertSeat> getAllSeats(long scheduleId) {
        return concertSeatJpaRepository.findAllByConcertSchedule_ScheduleId(scheduleId);
    }

    @Override
    public boolean getConcertIsAvailByConcertId(long concertId) {
        return concertJpaRepository.findIsAvailByConcertId(concertId);
    }

    @Override
    public long saveConcert(Concert concert) {
        em.persist(concert);
        em.flush();
        return concert.getConcertId();
    }
}
