package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
