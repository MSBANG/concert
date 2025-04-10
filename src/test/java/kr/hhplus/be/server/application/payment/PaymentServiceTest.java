package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.domain.ConcertSeat;
import kr.hhplus.be.server.domain.concert.domain.Reservation;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.interfaces.api.common.APIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private ConcertRepository concertRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @InjectMocks
    private PaymentService paymentService; // payReservation 메서드가 들어있는 서비스

    @Test
    void 예약이_만료된_경우_실패() {
        long userId = 1L, reservationId = 100L;
        Reservation expiredReservation = new Reservation(reservationId, userId, 1L, 1L, -1);
        ConcertSeat concertSeat = new ConcertSeat(1L, 1L, false, 10000L);
        Payment payment = new Payment(userId, 20000L);

        when(concertRepo.getReservationById(reservationId)).thenReturn(expiredReservation);
        when(concertRepo.getConcertSeatById(1L)).thenReturn(concertSeat);
        when(paymentRepo.getBalance(userId)).thenReturn(payment);
        assertThrows(APIException.class, () ->
                paymentService.payReservation(userId, reservationId));
    }

    @Test
    void 이미_결제된_예약건에_대한_결제_시도_실패() {
        long userId = 1L, reservationId = 100L;
        Reservation expiredReservation = new Reservation(reservationId, userId, 1L, 1L, 1);
        ConcertSeat concertSeat = new ConcertSeat(1L, 1L, false, 10000L);
        Payment payment = new Payment(userId, 20000L);

        when(concertRepo.getReservationById(reservationId)).thenReturn(expiredReservation);
        when(concertRepo.getConcertSeatById(1L)).thenReturn(concertSeat);
        when(paymentRepo.getBalance(userId)).thenReturn(payment);
        assertThrows(APIException.class, () ->
                paymentService.payReservation(userId, reservationId));
    }

    @Test
    void 좌석가격이_잔금보다_클_경우_실패() {
        long userId = 1L, reservationId = 100L;
        Reservation reservation = new Reservation(reservationId, userId, 1L, 1L, 0);
        ConcertSeat concertSeat = new ConcertSeat(1L, 1L, false, 20000L);
        Payment payment = new Payment(userId, 10000L);

        when(concertRepo.getReservationById(reservationId)).thenReturn(reservation);
        when(concertRepo.getConcertSeatById(1L)).thenReturn(concertSeat);
        when(paymentRepo.getBalance(userId)).thenReturn(payment);
        assertThrows(APIException.class, () ->
                paymentService.payReservation(userId, reservationId));;
    }

    @Test
    void 좌석가격만큼_잔금이_차감된다() {
        long userId = 1L, reservationId = 100L;
        Reservation reservation = new Reservation(reservationId, userId, 1L, 1L, 0);
        ConcertSeat concertSeat = new ConcertSeat(1L, 1L, false, 20_000L);
        Payment payment = mock(Payment.class);

        when(concertRepo.getReservationById(reservationId)).thenReturn(reservation);
        when(concertRepo.getConcertSeatById(1L)).thenReturn(concertSeat);
        when(paymentRepo.getBalance(userId)).thenReturn(payment);

        paymentService.payReservation(userId, reservationId);

        verify(payment).use(20_000L); // 잔금이 정확히 차감됐는지
        verify(paymentRepo).updateBalance(payment);
    }

    @Test
    void 예약상태가_정상적으로_변경된다() {
        long userId = 1L, reservationId = 100L;
        Reservation reservation = mock(Reservation.class);
        ConcertSeat seat = mock(ConcertSeat.class);
        Payment payment = mock(Payment.class);

        when(reservation.getSeatId()).thenReturn(10L);
        when(reservation.getReservationId()).thenReturn(reservationId);
        when(reservation.getStatusEnum()).thenReturn(1L);

        when(concertRepo.getReservationById(reservationId)).thenReturn(reservation);
        when(concertRepo.getConcertSeatById(10L)).thenReturn(seat);
        when(paymentRepo.getBalance(userId)).thenReturn(payment);

        paymentService.payReservation(userId, reservationId);

        verify(reservation).pay(); // 상태 변경 메서드 호출됐는지
        verify(concertRepo).updateReservationStatus(reservationId, 1);
    }
}