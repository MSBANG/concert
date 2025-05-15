package kr.hhplus.be.server.application.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ScheduleRemainSeatRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.support.APIException;
import kr.hhplus.be.server.support.distributedLock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final ReservationRepository reservationRepo;
    private final ScheduleRemainSeatRepository scheduleRemainSeatRepo;

    // 예약건에 대한 결제 요청
    // 목록에서 예약건을 확인한 다음, 예약건을 특정하여 Command 로 들어온 상태
    @DistributedLock
    @Transactional
    // payment : 충전, 중복 결제에서 충돌 생길 수 있음
    public void payForReservation(PaymentCommand.Pay command){
        Reservation reservation = reservationRepo.getReservationByUserIdAndReservationId(command.getUserId(), command.getReservationId())
                .orElseThrow(APIException::reservationNotFound);
        reservation.validateStatusEnum();
        Payment payment = paymentRepo.getPaymentByUserId(command.getUserId());
        payment.use(reservation.getSeat().getPrice());
        reservation.pay();

        // TODO: 결제가 된 이후, Redis 에서 잔여 좌석을 줄인다
        scheduleRemainSeatRepo.decrSeat(reservation.getSeat().getConcertSchedule().getScheduleId());
    }

    // 잔금 조회 요청
    public PaymentResult getBalance(PaymentCommand.Get command) {
        Payment payment = paymentRepo.getPaymentByUserId(command.getUserId());
        return PaymentResult.from(payment);
    }

    @DistributedLock
    @Transactional
    public PaymentResult chargeBalance(PaymentCommand.Charge command) {
        Payment payment = paymentRepo.getPaymentByUserId(command.getUserId());
        payment.charge(command.getAmount());
        return PaymentResult.from(payment);
    }
}
