package kr.hhplus.be.server.application.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.concert.ConcertJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertScheduleJpaRepository;
import kr.hhplus.be.server.infrastructure.concert.ConcertSeatJpaRepository;
import kr.hhplus.be.server.infrastructure.queue.QueueJPARepository;
import kr.hhplus.be.server.infrastructure.queue.QueueTokenGenerator;
import kr.hhplus.be.server.interfaces.api.common.ResponseCodeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Rollback(true)
class ReservationServiceIntegrationTest {


    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertSeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository scheduleJpaRepository;
    @Autowired
    private QueueJPARepository queueJPARepository;
    @Autowired
    private QueueTokenGenerator queueTokenGenerator;

    @Test
    @Transactional
    @DisplayName("좌석 예약이 정상적으로 진행된다")
    void seatReservationIntegrationTest() {
        // given
        User user = new User(1L);
        Concert concert = concertJpaRepository.save(Concert.create("AI 콘서트"));
        Queue queue = Queue.of(user, concert, false);
        String queueToken = queueTokenGenerator.encode(queue);
        ConcertSchedule schedule = scheduleJpaRepository.save(ConcertSchedule.create(concert, LocalDate.now()));
        ConcertSeat seat = seatJpaRepository.save(ConcertSeat.create(schedule, concert, 10000L, true, 10));

        ReservationCommand command = ReservationCommand.of(
                seat.getSeatId(),
                queueToken
        );

        // when
        reservationService.reserveSeat(command);

        // then
        Reservation saved = reservationRepository.findBySeatId(seat.getSeatId())
                .orElseThrow(() -> new RuntimeException(ResponseCodeEnum.SEAT_NOT_FOUND.getMessage()));

        Assertions.assertEquals(ReservationStatusEnum.RESERVED, saved.getStatusEnum());
        Assertions.assertFalse(saved.getSeat().isAvail()); // 좌석도 예약 상태로 변경됨
    }

    @Test
    @DisplayName("동시에_여러_요청이_좌석을_예약하면_오직_하나만_성공한다")
    void reserveSeatConcurrencyTest() throws InterruptedException {
        int THREAD_COUNT = 10;

        // given
        Concert concert = concertJpaRepository.save(Concert.create("동시성 테스트"));
        ConcertSchedule schedule = scheduleJpaRepository.save(ConcertSchedule.create(concert, LocalDate.now()));
        ConcertSeat seat = seatJpaRepository.save(ConcertSeat.create(schedule, concert, 100000L, true, 1));

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);  // 모든 스레드 준비 시
        CountDownLatch startLatch = new CountDownLatch(1);             // 시작 신호
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);   // 모두 완료될 때

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        for (int i = 0; i < THREAD_COUNT; i++) {
            final long userId = i + 1;
            User user = new User(userId);
            Queue queue = Queue.of(user, concert, false);
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    ReservationCommand command = ReservationCommand.of(
                            userId,
                            queueTokenGenerator.encode(queue)
                    );
                    reservationService.reserveSeat(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("❌ 실패한 유저: " + userId + ", error: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();  // 모든 스레드 준비될 때까지 대기
        startLatch.countDown();  // 시작 신호 발사!
        doneLatch.await();   // 모두 끝날 때까지 대기

        // then
        System.out.println("✅ 성공한 요청 수: " + successCount.get());
        System.out.println("❌ 실패한 요청 수: " + failCount.get());

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(THREAD_COUNT - 1);
    }
}
