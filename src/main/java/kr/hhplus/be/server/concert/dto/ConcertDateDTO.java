package kr.hhplus.be.server.concert.dto;

import java.time.DateTimeException;
import java.util.Date;

public record ConcertDateDTO(
        long date_id,
        String startDate
) {
    public static ConcertDateDTO getDefault() {
        return new ConcertDateDTO(1, "2025-04-03");
    }
}
