package kr.hhplus.be.server.application.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.domain.ConcertSeat;
import kr.hhplus.be.server.domain.concert.domain.Reservation;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final PaymentMapper paymentMapper;
    private final ConcertRepository concertRepo;

    @Autowired
    public PaymentService(PaymentRepository paymentRepo, PaymentMapper paymentMapper, ConcertRepository concertRepo) {
        this.paymentRepo = paymentRepo;
        this.paymentMapper = paymentMapper;
        this.concertRepo = concertRepo;
    }

    // 잔금 조회
    public PaymentCommand getBalance(long userId){
        Payment payment = paymentRepo.getBalance(userId);
        return paymentMapper.toCommand(payment);
    }

    // 잔금 충전
    public PaymentCommand chargeBalance(long userId, long amount) {
        Payment payment = paymentRepo.getBalance(userId);
        payment.charge(amount);
        paymentRepo.updateBalance(payment);
        return paymentMapper.toCommand(payment);
    }


    // 예약 결제
    @Transactional
    public void payReservation(long userId, long reservationId){
        // 예약 조회
        Reservation reservation = concertRepo.getReservationById(reservationId);
        // 예약된 좌석 조회
        ConcertSeat concertSeat = concertRepo.getConcertSeatById(reservation.getSeatId());
        // 잔금 조회
        Payment payment = paymentRepo.getBalance(userId);
        // 잔금 사용
        payment.use(concertSeat.getPrice());
        // 예약 entity 상태 변경
        reservation.pay();

        paymentRepo.updateBalance(payment);
        concertRepo.updateReservationStatus(reservation.getReservationId(), reservation.getStatusEnum());
    }
}
