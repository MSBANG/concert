package kr.hhplus.be.server.domain.reservation;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservationId;
    private long userId;


    @ManyToOne
    @JoinColumn(name = "seatId")
    private ConcertSeat seat;

    @ManyToOne
    @JoinColumn(name = "concertId")
    private Concert concert;

    @Enumerated(EnumType.STRING)
    private ReservationStatusEnum statusEnum;


    @Builder
    public Reservation(long userId, ConcertSeat seat, Concert concert, ReservationStatusEnum statusEnum) {
        this.userId = userId;
        this.seat = seat;
        this.concert = concert;
        this.statusEnum = statusEnum;
    }

    public static Reservation create(long userId, ConcertSeat seat, Concert concert, ReservationStatusEnum statusEnum){
        return Reservation.builder()
                .userId(userId)
                .seat(seat)
                .concert(concert)
                .statusEnum(statusEnum)
                .build();
    }
}

