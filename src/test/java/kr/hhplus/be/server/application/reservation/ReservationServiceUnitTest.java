package kr.hhplus.be.server.application.reservation;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.queue.QueueTokenGenerator;
import kr.hhplus.be.server.support.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ReservationServiceTest {

    @Mock
    private ConcertRepository concertRepo;

    @Mock
    private ReservationRepository reservationRepo;

    @InjectMocks
    private ReservationService reservationService;

    @Autowired
    private QueueTokenGenerator queueTokenGenerator;

    @Test
    @DisplayName("좌석이 없을 경우 seatNotFound 예외 발생")
    void testSeatNotFoundException() {
        // given
        long seatId = 1L;
        User user = new User(1L);
        Concert concert = Concert.create("TEST CONCERT");

        Queue queue = Queue.of(user, concert, false);

        ReservationCommand command = ReservationCommand.of(seatId, queueTokenGenerator.encode(queue));

        Mockito.when(concertRepo.getSeatById(seatId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.reserveSeat(command))
                .isInstanceOf(APIException.class);
    }

    @Test
    @DisplayName("콘서트가 없을 경우 concertNotFound 예외 발생")
    void testConcertNotFoundException() {
        // given
        long seatId = 1L;
        long concertId = 10L;
        User user = new User(1L);
        Concert concert = Concert.create("TEST CONCERT");
        Queue queue = Queue.of(user, concert, false);

        ReservationCommand command = ReservationCommand.of(100L, queueTokenGenerator.encode(queue));

        ConcertSeat seat = ConcertSeat.create(null, concert, 10000L, true, 1);
        Mockito.when(concertRepo.getSeatById(seatId)).thenReturn(Optional.of(seat));
        Mockito.when(concertRepo.getConcertById(concertId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.reserveSeat(command))
                .isInstanceOf(APIException.class);
    }

    @Test
    @DisplayName("정상적인 예약이 성공적으로 이루어지는 경우")
    void testReserveSeatSuccess() {
        // given
        long seatId = 1L;
        long concertId = 10L;
        long userId = 100L;

        User user = new User(1L);
        Concert concert = Concert.create("TEST CONCERT");
        Queue queue = Queue.of(user, concert, false);

        ReservationCommand command = ReservationCommand.of(userId, queueTokenGenerator.encode(queue));
        ConcertSchedule schedule = Mockito.mock(ConcertSchedule.class);
        ConcertSeat seat = ConcertSeat.create(null, concert, 10000L, true, 50);

        Mockito.when(concertRepo.getSeatById(seatId)).thenReturn(Optional.of(seat));
        Mockito.when(concertRepo.getConcertById(concertId)).thenReturn(Optional.of(concert));

        // when
        reservationService.reserveSeat(command);

        // then
        Mockito.verify(reservationRepo).save(Mockito.any(Reservation.class));
    }
}

