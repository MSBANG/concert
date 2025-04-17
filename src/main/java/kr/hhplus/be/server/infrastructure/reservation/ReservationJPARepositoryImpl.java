package kr.hhplus.be.server.infrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationJPARepositoryImpl implements ReservationRepository {
    @Override
    public List<Reservation> getReservationsByUserId(long userId) {
        return List.of();
    }

    @Override
    public Optional<Reservation> getReservationByUserIdAndReservationId(long userId, long reservationId) {
        return Optional.empty();
    }

    @Override
    public void save(Reservation reservation) {

    }
}
