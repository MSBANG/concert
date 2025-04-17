package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.interfaces.api.common.APIException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ConcertSchedule concertSchedule;

    private long price;
    private boolean isAvail;
    private int seatNum;

    @OneToMany(mappedBy = "seat")
    List<Reservation> reservations;

    @Builder
    private ConcertSeat(ConcertSchedule concertSchedule, long price, boolean isAvail, int seatNum) {
        this.concertSchedule = concertSchedule;
        this.price = price;
        this.isAvail = isAvail;
        this.seatNum = seatNum;
    }

    public static ConcertSeat create(ConcertSchedule concertSchedule, long price, boolean isAvail, int seatNum) {
        return ConcertSeat.builder()
                .concertSchedule(concertSchedule)
                .price(price)
                .isAvail(isAvail)
                .seatNum(seatNum)
                .build();
    }

    public void reserve() {
        checkReserveStatus();
        this.isAvail = false;
    }

    private void checkReserveStatus() {
        if (!(this.isAvail)) {
            throw APIException.seatAlreadyReserved();
        }
    }
}
