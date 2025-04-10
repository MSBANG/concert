package kr.hhplus.be.server.domain.concert;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConcertWithDate(
        long concertId,
        ConcertDate[] dates
){
}