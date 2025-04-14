package kr.hhplus.be.server.application.concert.command;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConcertCommand(
        long concertId,
        String concertName,
        LocalDate sDate,
        LocalDate eDate,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}

