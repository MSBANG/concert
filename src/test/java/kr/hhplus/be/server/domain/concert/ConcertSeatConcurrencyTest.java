package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import kr.hhplus.be.server.infrastructure.concert.ConcertJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertScheduleJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertSeatJpaRepository;
import kr.hhplus.be.server.infrastructure.reservation.ReservationJPARepository;
import org.hibernate.annotations.OptimisticLock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ConcertSeatConcurrencyTest {
    @Autowired
    ConcertJpaRepository concertJpaRepository;

    @Autowired
    ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    ConcertSeatJpaRepository concertSeatJpaRepository;

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    ReservationJPARepository reservationJPARepository;

    @Test
    void 동시에_같은_좌석을_예약하면_1개만_성공하고_나머지는_실패한다() {
        // given
        long userId1 = 1L;
        long userId2 = 2L;
        Concert concert = Concert.create("테스트 콘서트");
        ConcertSchedule schedule = ConcertSchedule.create(concert, LocalDate.now());
        ConcertSeat seat = ConcertSeat.create(schedule, concert, 10000L, true, 1);

        concertJpaRepository.save(concert);
        concertScheduleJpaRepository.save(schedule);
        ConcertSeat seatInDB = concertSeatJpaRepository.save(seat);

        EntityManager em1 = emf.createEntityManager();
        EntityManager em2 = emf.createEntityManager();

        em1.getTransaction().begin();
        em2.getTransaction().begin();

        ConcertSeat s1 = em1.find(ConcertSeat.class, seatInDB.getSeatId());
        ConcertSeat s2 = em2.find(ConcertSeat.class, seatInDB.getSeatId());

        // when
        s1.reserve();
        em1.getTransaction().commit();
        em1.close();

        Reservation r1 = Reservation.create(userId1, seat, concert, ReservationStatusEnum.RESERVED, LocalDateTime.now().plusMinutes(5));
        reservationJPARepository.save(r1);
        
        Reservation r2 = Reservation.create(userId2, seat, concert, ReservationStatusEnum.RESERVED, LocalDateTime.now().plusMinutes(5));
        Exception occuredException = null;

        s2.reserve();
        try{
            em2.getTransaction().commit();
            em2.close();
            reservationJPARepository.save(r2);
        }catch (RollbackException e) {
            Throwable cause = e.getCause();
            if (cause instanceof OptimisticLockException) {
                occuredException = new OptimisticLockException();
            } else {throw e;}
        }

        // then
        ConcertSeat seatResult = concertSeatJpaRepository.findById(seatInDB.getSeatId()).orElseThrow();

        Assertions.assertFalse(seatResult.isAvail());
        assertThat(r1.getReservationId())
                .as("먼저 예약한 기록 생성 확인")
                .isNotEqualByComparingTo(0L);

        assertThat(r2.getReservationId())
                .as("OptimisticLock 발생 이후 예약은 추가 생성 되지 않음")
                .isEqualTo(0L);

        Assertions.assertTrue(occuredException instanceof OptimisticLockException);
    }
}
