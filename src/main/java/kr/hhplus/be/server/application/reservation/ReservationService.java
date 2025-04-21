package kr.hhplus.be.server.application.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import kr.hhplus.be.server.support.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ConcertRepository concertRepo;
    private final ReservationRepository reservationRepo;

    @Transactional
    public void reserveSeat(ReservationCommand command) {
        ConcertSeat seat = this.concertRepo.getSeatById(command.getSeatId())
                .orElseThrow(APIException::seatNotFound);
        seat.reserve();
        Concert concert = this.concertRepo.getConcertById(command.getConcertId())
                .orElseThrow(APIException::concertNotFound);
        Reservation reservation = Reservation.create(
                command.getUserId(),
                seat,
                concert,
                ReservationStatusEnum.RESERVED
        );
        concertRepo.updateSeatIsAvail(seat.getSeatId(), seat.isAvail());
        reservationRepo.save(reservation);
    }

    // 사용자 예약 목록
    public List<ReservationResult> getReservations(ReservationCommand command) {
        List<Reservation> reservations = reservationRepo.getReservationsByUserId(command.getUserId());
        return reservations.stream()
                .map(ReservationResult::from)
                .toList();
    }
}
