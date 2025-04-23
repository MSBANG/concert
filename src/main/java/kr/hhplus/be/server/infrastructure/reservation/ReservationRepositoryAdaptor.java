package kr.hhplus.be.server.infrastructure.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryAdaptor implements ReservationRepository {
    @PersistenceContext
    private EntityManager em;

    private final ReservationJPARepository reservationJpaRepository;

    @Override
    public List<Reservation> getReservationsByUserId(long userId) {
        return reservationJpaRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Reservation> getReservationByUserIdAndReservationId(long userId, long reservationId) {
        return reservationJpaRepository.findByUserIdAndReservationId(userId, reservationId);
    }

    @Override
    public long save(Reservation reservation) {
        em.persist(reservation);
        em.flush();
        return reservation.getReservationId();
    }

    @Override
    public Optional<Reservation> findBySeatId(long seatId) {
        return reservationJpaRepository.findBySeat_SeatId(seatId);
    }

    @Override
    public List<Reservation> getAllExpiredReservations() {
        return reservationJpaRepository.findByStatusEnumAndExpiresInLessThan(ReservationStatusEnum.RESERVED, LocalDateTime.now());
    }

    @Override
    public Reservation getReservationById(long reservationId) {
        return reservationJpaRepository.findByReservationId(reservationId);
    }
}
