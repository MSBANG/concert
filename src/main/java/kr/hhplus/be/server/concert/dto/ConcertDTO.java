package kr.hhplus.be.server.concert.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public record ConcertDTO(
        long concert_id,
        String concert_name,
        List<ConcertDateDTO> concertDates
) {

    public static ConcertDTO getDefault(){
        List<ConcertDateDTO> defaultDates = Arrays.asList(
                ConcertDateDTO.getDefault(),
                ConcertDateDTO.getDefault()
        );
        return new ConcertDTO(1L, "콘서트 이름", defaultDates);
    };
}
