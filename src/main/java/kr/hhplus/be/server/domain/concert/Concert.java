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