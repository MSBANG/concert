package kr.hhplus.be.server.concert.dto;

public record ConcertSeatDTO(
        long seatNum,
        boolean isAvail
) {
    public static ConcertSeatDTO getDefault() { return new ConcertSeatDTO(1L, true); }
}
