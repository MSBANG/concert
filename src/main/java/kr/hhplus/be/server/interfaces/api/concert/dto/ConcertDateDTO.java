package kr.hhplus.be.server.interfaces.api.concert.dto;

public record ConcertDateDTO(
        long date_id,
        String startDate
) {
    public static ConcertDateDTO getDefault() {
        return new ConcertDateDTO(1, "2025-04-03");
    }
}
