package kr.hhplus.be.server.domain.concert.domain;


import kr.hhplus.be.server.interfaces.api.common.APIException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Concert {
    private long concertId;
    private String name;
    private LocalDate sDate;
    private LocalDate eDate;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    protected Concert(
        long concertId,
        String name,
        LocalDate sDate,
        LocalDate eDate,
        LocalDateTime created_at,
        LocalDateTime updated_at
    ) {}


    public static Concert of(
            long concertId,
            String name,
            LocalDate sDate,
            LocalDate eDate,
            LocalDateTime created_at,
            LocalDateTime updated_at
    ) {
        // 시작일 이 종료일 이후일 수 없음
        if (sDate.isAfter(eDate)) {
            throw APIException.startDateAfterEndDate();
        }
        return new Concert(concertId, name, sDate, eDate, created_at, updated_at);
    }
}

