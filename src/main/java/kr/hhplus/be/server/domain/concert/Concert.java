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

    @Builder
    private Concert(long concertId, String name ) {
        this.concertId = concertId;
        this.name = name;
    }

    public static Concert create(long concertId, String name){
        Concert.validateName(name);
        return Concert.builder()
                .concertId(concertId)
                .name(name)
                .build();
    }

    private static void validateName(String name) {
        if(name.isEmpty()) {
            throw new RuntimeException("fuck");
        }
    }
}