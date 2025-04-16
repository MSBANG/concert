package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class ConcertSchedule extends BaseEntity {
    @Id
    private long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concertId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Concert concert;

    private LocalDate startDate;

    @Builder
    private ConcertSchedule(long scheduleId, Concert concert, LocalDate startDate) {
        this.scheduleId = scheduleId;
        this.concert = concert;
        this.startDate = startDate;
    }

    public static ConcertSchedule create(long scheduleId, Concert concert, LocalDate startDate){
        return ConcertSchedule.builder()
                .scheduleId(scheduleId)
                .concert(concert)
                .startDate(startDate)
                .build();
    }
}
