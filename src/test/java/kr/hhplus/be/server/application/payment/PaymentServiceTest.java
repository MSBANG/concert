package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.interfaces.api.common.ResponseCodeEnum;
import kr.hhplus.be.server.support.APIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepo;
    private ReservationRepository reservationRepo;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepo = mock(PaymentRepository.class);
        reservationRepo = mock(ReservationRepository.class);
        paymentService = new PaymentService(paymentRepo, reservationRepo);
    }

    @Test
    @DisplayName("payForReservation - 예약 존재하지 않으면 예외 발생")
    void payForReservation_should_throw_if_reservation_not_found() {
        // given
        long userId = 1L;
        long reservationId = 100L;
        PaymentCommand.Pay command = PaymentCommand.Pay.of(userId, reservationId);
        when(reservationRepo.getReservationByUserIdAndReservationId(userId, reservationId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentService.payForReservation(command))
                .isInstanceOf(APIException.class)
                .hasMessageContaining(ResponseCodeEnum.RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("payForReservation - 정상 결제 흐름")
    void payForReservation_should_use_payment_and_update_balance() {
        // given
        long userId = 1L;
        long reservationId = 100L;
        long seatPrice = 5000L;

        ConcertSeat seat = mock(ConcertSeat.class);
        when(seat.getPrice()).thenReturn(seatPrice);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getSeat()).thenReturn(seat);
        doNothing().when(reservation).validateStatusEnum();

        Payment payment = Payment.of(userId, 10000L); // 충분한 금액 보유

        when(reservationRepo.getReservationByUserIdAndReservationId(userId, reservationId))
                .thenReturn(Optional.of(reservation));
        when(paymentRepo.getPaymentByUserId(userId)).thenReturn(payment);

        // when
        paymentService.payForReservation(PaymentCommand.Pay.of(userId, reservationId));

        // then
        assertThat(payment.getBalance()).isEqualTo(5000L);
        verify(paymentRepo).updateBalance(userId, 5000L);
    }

    @Test
    @DisplayName("getBalance - 잔액 조회 결과 반환")
    void getBalance_should_return_payment_result() {
        // given
        long userId = 1L;
        Payment payment = Payment.of(userId, 3000L);
        when(paymentRepo.getPaymentByUserId(userId)).thenReturn(payment);

        // when
        PaymentResult result = paymentService.getBalance(PaymentCommand.Get.of(userId));

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getBalance()).isEqualTo(3000L);
    }

    @Test
    @DisplayName("chargeBalance - 충전하면 잔액 증가하고 업데이트됨")
    void chargeBalance_should_increase_balance_and_update() {
        // given
        long userId = 1L;
        long amount = 2000L;
        Payment payment = Payment.of(userId, 3000L);

        when(paymentRepo.getPaymentByUserId(userId)).thenReturn(payment);

        // when
        PaymentResult result = paymentService.chargeBalance(PaymentCommand.Charge.of(userId, amount));

        // then
        assertThat(result.getBalance()).isEqualTo(5000L);
        verify(paymentRepo).updateBalance(userId, 5000L);
    }
}
