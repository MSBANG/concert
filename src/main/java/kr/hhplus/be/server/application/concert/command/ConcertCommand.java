package kr.hhplus.be.server.application.concert.command;


import kr.hhplus.be.server.interfaces.api.common.APIException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConcertCommand {
    private long concertId;
    private String name;
    private LocalDate sDate;
    private LocalDate eDate;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    protected ConcertCommand(
        long concertId,
        String name,
        LocalDate sDate,
        LocalDate eDate,
        LocalDateTime created_at,
        LocalDateTime updated_at
    ) {}


    public static ConcertCommand of(
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
        return new ConcertCommand(concertId, name, sDate, eDate, created_at, updated_at);
    }
}

