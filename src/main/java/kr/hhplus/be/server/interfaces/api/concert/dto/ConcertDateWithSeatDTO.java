package kr.hhplus.be.server.interfaces.api.concert.dto;

import java.util.Arrays;
import java.util.List;

public record ConcertDateWithSeatDTO(
        long date_id,
        String startDate,
        List<ConcertSeatDTO> concertSeats
) {
    public static ConcertDateWithSeatDTO getDefault() {
        List<ConcertSeatDTO> concertSeats = Arrays.asList(
                ConcertSeatDTO.getDefault(),
                ConcertSeatDTO.getDefault(),
                ConcertSeatDTO.getDefault()
        );
        return new ConcertDateWithSeatDTO(1L, "2025-04-03", concertSeats);
    };
}
