package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.concert.*;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import kr.hhplus.be.server.infrastructure.concert.ConcertJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertScheduleJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertSeatJpaRepository;
import kr.hhplus.be.server.infrastructure.payment.PaymentJPARepository;
import kr.hhplus.be.server.infrastructure.reservation.ReservationJPARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private PaymentJPARepository paymentJPARepository;
    @Autowired
    private ReservationRepository reservationRepo;
    @Autowired
    private ScheduleRemainSeatRepository scheduleRemainSeatRepository;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationJPARepository reservationJPARepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private ConcertJpaRepository concertJpaRepository;
    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;


    private long seatCount = 50;
    private long userId = 1;
    private Concert concert;
    private ConcertSchedule schedule;
    private ConcertSeat seat;
    private Reservation reservation;
    private Payment payment;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        concert = Concert.create("testConcert");
        schedule = ConcertSchedule.create(concert, LocalDate.of(2025, 5, 15), seatCount);
        seat = ConcertSeat.create(schedule, concert, 10000, true, 10 );
        reservation = Reservation.create(userId, seat, concert, ReservationStatusEnum.RESERVED, LocalDateTime.now().plusMinutes(10));
        payment = Payment.create(1L, 100000L);


        concertJpaRepository.save(concert);
        concertScheduleJpaRepository.save(schedule);
        concertSeatJpaRepository.save(seat);
        reservationJPARepository.save(reservation);
        paymentJPARepository.save(payment);
    }

    @Test
    @DisplayName("예약건이 결제된 이후 잔여 좌석이 감소한다")
    void decrRemainSeatAfterReservationPaid() {
        //given

        scheduleRemainSeatRepository.setRemainSeat(schedule.getScheduleId(), seatCount);

        PaymentCommand.Pay command = PaymentCommand.Pay.of(userId, reservation.getReservationId());

        //when
        paymentService.payForReservation(command);

        //then
        Assertions.assertEquals(seatCount - 1, scheduleRemainSeatRepository.getSeatRemain(schedule.getScheduleId()));
    }

    @Test
    @DisplayName("모든 좌석이 매진된 이후 스케줄이 기록된다")
    void scheduleRankedAfterAllSeatSoldOut() {
        //given

        scheduleRemainSeatRepository.setRemainSeat(schedule.getScheduleId(), 1L);

        PaymentCommand.Pay command = PaymentCommand.Pay.of(userId, reservation.getReservationId());

        //when
        paymentService.payForReservation(command);

        //then
        Assertions.assertEquals(Set.of("scheduleId:%s:soldOutTime".formatted(schedule.getScheduleId())), scheduleRemainSeatRepository.getTodaySoldOutScheduleSet());
    }

}
