package kr.hhplus.be.server.domain.reservation;


import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> getReservationsByUserId(long userId);
    Optional<Reservation> getReservationByUserIdAndReservationId(long userId, long reservationId);
    void save(Reservation reservation);

    Optional<Reservation> findBySeatId(long seatId);
}
