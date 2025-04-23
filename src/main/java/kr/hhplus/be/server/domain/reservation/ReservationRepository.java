package kr.hhplus.be.server.domain.reservation;


import java.util.List;
import java.util.Optional;


public interface ReservationRepository {
    List<Reservation> getReservationsByUserId(long userId);
    Optional<Reservation> getReservationByUserIdAndReservationId(long userId, long reservationId);
    long save(Reservation reservation);
    Optional<Reservation> findBySeatId(long seatId);
    List<Reservation> getAllExpiredReservations();
    Reservation getReservationById(long reservationId);
}
