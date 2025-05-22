package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class ConcertSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concertId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Concert concert;

    private LocalDate startDate;

    private long seatCount;

    @Builder
    private ConcertSchedule(Concert concert, LocalDate startDate, long seatCount) {
        this.concert = concert;
        this.startDate = startDate;
        this.seatCount = seatCount;
    }

    public static ConcertSchedule create(Concert concert, LocalDate startDate){
        return ConcertSchedule.builder()
                .concert(concert)
                .startDate(startDate)
                .build();
    }

    public static ConcertSchedule create(Concert concert, LocalDate startDate, long seatCount){
        return ConcertSchedule.builder()
                .concert(concert)
                .seatCount(seatCount)
                .startDate(startDate)
                .build();
    }
}
