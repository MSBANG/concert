package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import lombok.*;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long concertId;
    private String name;

    @OneToMany(mappedBy = "concert")
    List<ConcertSchedule> schedules;

    @Builder
    private Concert(long concertId, String name ) {
        this.concertId = concertId;
        this.name = name;
    }

    public static Concert create(long concertId, String name){
        validateName(name);
        return Concert.builder()
                .concertId(concertId)
                .name(name)
                .build();
    }

    private static void validateName(String name) {
        if(name.isEmpty()) {
            throw new RuntimeException("invalid concert name");
        }
    }
}