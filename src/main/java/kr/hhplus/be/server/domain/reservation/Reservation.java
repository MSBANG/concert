package kr.hhplus.be.server.domain.reservation;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.support.APIException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservationId;
    private long userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seatId")
    private ConcertSeat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concertId")
    private Concert concert;

    @Enumerated(EnumType.STRING)
    private ReservationStatusEnum statusEnum;

    @Version
    @Column(name = "OPTLOCK")
    private long version;

    private LocalDateTime expiresIn;

    @Builder
    private Reservation(long reservationId, long userId, ConcertSeat seat, Concert concert, ReservationStatusEnum statusEnum, LocalDateTime expiresIn) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.seat = seat;
        this.concert = concert;
        this.statusEnum = statusEnum;
        this.expiresIn = expiresIn;
    }

    public static Reservation create(long userId, ConcertSeat seat, Concert concert, ReservationStatusEnum statusEnum, LocalDateTime expiresIn){
        return Reservation.builder()
                .userId(userId)
                .seat(seat)
                .concert(concert)
                .statusEnum(statusEnum)
                .expiresIn(expiresIn)
                .build();
    }

    public static Reservation of(long reservationId, long userId, ConcertSeat seat, ReservationStatusEnum statusEnum) {
        return Reservation.builder()
                .reservationId(reservationId)
                .userId(userId)
                .seat(seat)
                .statusEnum(statusEnum)
                .build();
    }

    public void validateStatusEnum() {
        if (this.statusEnum != ReservationStatusEnum.RESERVED){
            if (this.statusEnum == ReservationStatusEnum.PAID) {
                throw APIException.alreadyPaidReservation();
            } else if(this.statusEnum == ReservationStatusEnum.EXPIRED) {
                throw APIException.expiredReservation();
            }
        }
    }

    public void expire() {
        this.statusEnum = ReservationStatusEnum.EXPIRED;
    }

    public void pay() {
        this.statusEnum = ReservationStatusEnum.PAID;
    }
}

