package kr.hhplus.be.server.domain.concert.domain;


import java.util.List;

public record ConcertWithDate(
        long concertId,
        List<ConcertDate> dates
){
}