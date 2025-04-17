package kr.hhplus.be.server.application.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import kr.hhplus.be.server.infrastructure.concert.ConcertJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertScheduleJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertSeatJpaRepository;
import kr.hhplus.be.server.interfaces.api.common.ResponseCodeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

@SpringBootTest
@Transactional
@Rollback(false)
class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ConcertSeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository scheduleJpaRepository;

    @Test
    @DisplayName("좌석 예약이 정상적으로 진행된다")
    void seatReservationIntegrationTest() {
        // given
        Concert concert = concertJpaRepository.save(Concert.create("AI 콘서트"));
        ConcertSchedule schedule = scheduleJpaRepository.save(ConcertSchedule.create(concert, LocalDate.now()));
        ConcertSeat seat = seatJpaRepository.save(
                ConcertSeat.create(0, schedule, 15000, true, 1)
        );

        ReservationCommand command = ReservationCommand.of(
                1L,
                seat.getSeatId(),
                concert.getConcertId()
        );

        // when
        reservationService.reserveSeat(command);

        // then
        Reservation saved = reservationRepository.findBySeatId(seat.getSeatId())
                .orElseThrow(() -> new RuntimeException(ResponseCodeEnum.SEAT_NOT_FOUND.getMessage()));

        Assertions.assertEquals(ReservationStatusEnum.RESERVED, saved.getStatusEnum());
        Assertions.assertFalse(saved.getSeat().isAvail()); // 좌석도 예약 상태로 변경됨
    }
}
