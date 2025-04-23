package kr.hhplus.be.server.application.reservation;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import kr.hhplus.be.server.infrastructure.queue.QueueTokenGenerator;
import kr.hhplus.be.server.support.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ConcertRepository concertRepo;
    private final ReservationRepository reservationRepo;
    private final QueueRepository queueRepository;
    private final QueueTokenGenerator queueTokenGenerator;

    @Transactional
    public void reserveSeat(ReservationCommand command) {
        Queue queue = queueTokenGenerator.decode(command.getQueueToken(), Queue.class);

        // 대기열 토큰의 대기열 정보가 잘못됐거나 만료되어 삭제됨
        queueRepository.getQueueById(queue.getQueueId())
                .orElseThrow(APIException::queueExpiredOrInvalid
        );

        if (!queue.isInProgress()) {
            throw APIException.queueTokenNotReady();
        }
        ConcertSeat seat = this.concertRepo.getSeatById(command.getSeatId())
                .orElseThrow(APIException::seatNotFound);
        seat.reserve();
        
        // 토큰에 있는 콘서트 ID 와 예약 요청한 좌석의 ConcertID 가 다름
        long seatConcertId = seat.getConcertSchedule().getConcert().getConcertId();
        Concert concert = this.concertRepo.getConcertById(seatConcertId)
                .orElseThrow(APIException::concertNotFound);

        if (seatConcertId != queue.getConcert().getConcertId()){
            throw APIException.invalidReservationExecuting();
        }
        Reservation reservation = Reservation.create(
                queue.getUser().getUserId(),
                seat,
                concert,
                ReservationStatusEnum.RESERVED,
                LocalDateTime.now().plusMinutes(5)
        );
        reservationRepo.save(reservation);
        queueRepository.remove(queue); //polling
    }

    // 사용자 예약 목록
    public List<ReservationResult> getReservations(long userId) {
        List<Reservation> reservations = reservationRepo.getReservationsByUserId(userId);
        return reservations.stream()
                .map(ReservationResult::from)
                .toList();
    }
}
