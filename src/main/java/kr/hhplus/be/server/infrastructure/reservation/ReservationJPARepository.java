package kr.hhplus.be.server.infrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(long userId);
    Optional<Reservation> findByUserIdAndReservationId(long userId, long reservationId);

    Optional<Reservation> findBySeat_SeatId(long seatId);
}
