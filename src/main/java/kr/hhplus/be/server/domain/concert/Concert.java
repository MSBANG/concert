package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import kr.hhplus.be.server.domain.reservation.Reservation;
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
    
    // 전체 좌석 예약 가능 여부, 스케줄러로 변경
    @Column(columnDefinition = "boolean default true")
    private boolean isAvail;

    @OneToMany(mappedBy = "concert")
    List<ConcertSchedule> schedules;

    @OneToMany(mappedBy = "concert")
    List<Reservation> reservations;

    @Builder
    private Concert(String name ) {
        this.name = name;
    }

    public static Concert create(String name){
        validateName(name);
        return Concert.builder()
                .name(name)
                .build();
    }

    private static void validateName(String name) {
        if(name.isEmpty()) {
            throw new RuntimeException("invalid concert name");
        }
    }
}