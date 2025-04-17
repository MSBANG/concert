package kr.hhplus.be.server.interfaces.api.concert;

import kr.hhplus.be.server.application.concert.ScheduleResult;
import kr.hhplus.be.server.domain.concert.Concert;

import java.time.LocalDate;

public record ScheduleResponse(
        long scheduleId,
        Concert concert,
        LocalDate startDate
) {
    public static ScheduleResponse from (ScheduleResult scheduleResult) {
        return new ScheduleResponse(
                scheduleResult.getScheduleId(),
                scheduleResult.getConcert(),
                scheduleResult.getStartDate()
            );
    }
}
