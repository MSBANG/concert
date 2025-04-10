package kr.hhplus.be.server.domain.concert.domain;


import kr.hhplus.be.server.interfaces.api.common.APIException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Concert(
        long concertId,
        String concertName,
        LocalDate sDate,
        LocalDate eDate,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
    public Concert{
        if (sDate.isAfter(eDate)){
            throw APIException.startDateAfterEndDate();
        }
    }
}

