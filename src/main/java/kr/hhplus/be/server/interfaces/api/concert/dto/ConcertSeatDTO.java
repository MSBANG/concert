package kr.hhplus.be.server.interfaces.api.concert.dto;

public record ConcertSeatDTO(
        long seatNum,
        boolean isAvail,
        long price
) {
    public static ConcertSeatDTO getDefault() { return new ConcertSeatDTO(1L, true, 160_000); }
}
