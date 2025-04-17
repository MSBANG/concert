package kr.hhplus.be.server.application.reservation;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.support.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Test
    @DisplayName("좌석이 없을 경우 seatNotFound 예외 발생")
    void testSeatNotFoundException() {
        // given
        long seatId = 1L;
        long concertId = 10L;
        ReservationCommand command = ReservationCommand.of(100L, seatId, concertId);

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
        ReservationCommand command = ReservationCommand.of(100L, seatId, concertId);

        ConcertSeat seat = ConcertSeat.create(seatId, null, 100L, true, 1);
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
        ReservationCommand command = ReservationCommand.of(userId, seatId, concertId);

        ConcertSeat seat = ConcertSeat.create(seatId, null, 100L, true, 1);
        Concert concert = Mockito.mock(Concert.class);

        Mockito.when(concertRepo.getSeatById(seatId)).thenReturn(Optional.of(seat));
        Mockito.when(concertRepo.getConcertById(concertId)).thenReturn(Optional.of(concert));

        // when
        reservationService.reserveSeat(command);

        // then
        Mockito.verify(concertRepo).updateSeatIsAvail(seatId, false);
        Mockito.verify(reservationRepo).save(Mockito.any(Reservation.class));
    }
}

