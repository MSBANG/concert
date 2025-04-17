package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.support.APIException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "concert_seat")
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
    private ConcertSeat(long seatId, ConcertSchedule concertSchedule, long price, boolean isAvail, int seatNum) {
        this.seatId = seatId;
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
        checkSeatAlreadyReserved();
        this.isAvail = false;
    }

    public void cancel() {
        checkSeatAlreadyCanceled();
        this.isAvail = true;
    }

    private void checkSeatAlreadyReserved() {
        if (!(this.isAvail)) {
            throw APIException.seatAlreadyReserved();
        }
    }

    private void checkSeatAlreadyCanceled() {
        if ((this.isAvail)) {
            throw new RuntimeException("some Exception");
        }
    }
}
