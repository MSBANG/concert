package kr.hhplus.be.server.domain.reservation;


import java.util.List;

public interface ReservationRepository {
    List<Reservation> getReservationsByUserId(long userId);
    void save(Reservation reservation);
}
