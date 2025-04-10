package kr.hhplus.be.server.interfaces.api.concert.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


public record ConcertDTO(
        long concertId,
        String concertName,
        LocalDate sDate,
        LocalDate eDate,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        List<ConcertDateDTO> concertDates
) {

    public static ConcertDTO getDefault(){
        List<ConcertDateDTO> defaultDates = Arrays.asList(
                ConcertDateDTO.getDefault(),
                ConcertDateDTO.getDefault()
        );
        LocalDate sDate = LocalDate.of(2025,4,1);
        LocalDate eDate = LocalDate.of(2025,4,5);
        LocalDateTime created_at = LocalDateTime.of(2025,4,5, 1,1,1);
        LocalDateTime updated_at = LocalDateTime.of(2025,4,5,1,1,1);
        return new ConcertDTO(1L, "콘서트 이름", sDate, eDate, created_at, updated_at, defaultDates);
    };
}
