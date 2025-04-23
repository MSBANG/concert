package kr.hhplus.be.server.interfaces.scheduler.reservation;

import kr.hhplus.be.server.application.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationService reservationService;

    @Scheduled(fixedDelay = 300000)
    public void expireReservationScheduler() {
        reservationService.expireReservations();
    }
}
