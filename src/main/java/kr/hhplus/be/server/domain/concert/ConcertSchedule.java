package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "concertSchedule")
    private List<ConcertSeat> seats;

    @Builder
    private ConcertSchedule(Concert concert, LocalDate startDate) {
        this.concert = concert;
        this.startDate = startDate;
    }

    public static ConcertSchedule create(Concert concert, LocalDate startDate){
        return ConcertSchedule.builder()
                .concert(concert)
                .startDate(startDate)
                .build();
    }
}
