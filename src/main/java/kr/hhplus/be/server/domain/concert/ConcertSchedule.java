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

    @Builder
    private ConcertSchedule(Long scheduleId, Concert concert, LocalDate startDate) {
        this.scheduleId = scheduleId;
        this.concert = concert;
        this.startDate = startDate;
    }

    public static ConcertSchedule create(Concert concert, LocalDate startDate){
        return ConcertSchedule.builder()
                .concert(concert)
                .startDate(startDate)
                .build();
    }

    public static ConcertSchedule create(long scheduleId, LocalDate startDate){
        return ConcertSchedule.builder()
                .scheduleId(scheduleId)
                .startDate(startDate)
                .build();
    }
}
