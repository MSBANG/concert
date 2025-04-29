package kr.hhplus.be.server.infrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(long userId);
    Optional<Reservation> findByUserIdAndReservationId(long userId, long reservationId);

    Optional<Reservation> findBySeat_SeatId(long seatId);

    List<Reservation> findByStatusEnumAndExpiresInLessThan(ReservationStatusEnum reservationStatusEnum, LocalDateTime now);

    Reservation findByReservationId(long reservationId);
}
