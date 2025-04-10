package kr.hhplus.be.server.domain.concert.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConcertDate(
        long dateId,
        LocalDate sDate,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
    // 생성할 때 Concert startDate 와 endDate 사이에 있어야 하지않나?
    // 그럼 생성할 때 같이 받아서 만들어야하나?
}
