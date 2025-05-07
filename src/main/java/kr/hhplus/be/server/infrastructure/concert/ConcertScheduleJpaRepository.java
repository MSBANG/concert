package kr.hhplus.be.server.infrastructure.concert;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ScheduleDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ScheduleDTO> findAllByConcert_ConcertId(@Param("concertId") Long concertId);
}
