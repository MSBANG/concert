package kr.hhplus.be.server.application.concert.command;


public record ConcertWithDateCommand(
        long concertId,
        ConcertDateCommand[] dates
){
}