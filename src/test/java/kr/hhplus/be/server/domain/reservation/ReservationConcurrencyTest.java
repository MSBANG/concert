package kr.hhplus.be.server.domain.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.infrastructure.concert.ConcertJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertScheduleJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertSeatJpaRepository;
import kr.hhplus.be.server.infrastructure.reservation.ReservationJPARepository;
import kr.hhplus.be.server.infrastructure.reservation.ReservationRepositoryAdaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ReservationConcurrencyTest {

    @Autowired
    ReservationRepositoryAdaptor reservationRepositoryAdaptor;

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private ReservationJPARepository reservationJPARepository;

    @Test
    void expire와_pay_동시_요청시_상태_갱신_경합_확인() throws InterruptedException {
        // given
        Concert concert = Concert.create("테스트 콘서트");
        ConcertSchedule schedule = ConcertSchedule.create(concert, LocalDate.now());
        ConcertSeat seat = ConcertSeat.create(schedule, concert, 10000L, true, 1);
        Reservation reservation = Reservation.create(1L, seat, concert, ReservationStatusEnum.RESERVED, LocalDateTime.now().plusMinutes(5));

        concertJpaRepository.save(concert);
        concertScheduleJpaRepository.save(schedule);
        seatJpaRepository.save(seat);
        reservationJPARepository.save(reservation);

        EntityManager em1 = emf.createEntityManager();
        EntityManager em2 = emf.createEntityManager();

        em1.getTransaction().begin();
        em2.getTransaction().begin();

        Reservation r1 = em1.find(Reservation.class, reservation.getReservationId());
        Reservation r2 = em2.find(Reservation.class, reservation.getReservationId());

        //when
        r1.pay();
        em1.getTransaction().commit();
        em1.close();

        r2.expire(); // 여기서 에러가 발생해야함 OptimisticLock
        try {
            em2.getTransaction().commit();
            em2.close();
        } catch (RollbackException e) {
            Throwable cause = e.getCause();
            if (cause instanceof OptimisticLockException) {
                System.out.println("낙관적 락 예외 발생");
            } else {
                throw e;
            }
        }

        // then
        Reservation result = reservationRepositoryAdaptor.getReservationById(reservation.getReservationId());
        System.out.println("최종 상태 = " + result.getStatusEnum());
        assertThat(result.getStatusEnum())
                .as("결제 완료 상태여야 한다")
                .isEqualTo(ReservationStatusEnum.PAID);  // 실패 가능
    }
}
