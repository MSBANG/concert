package kr.hhplus.be.server.application.concert;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleCommand {
    private long scheduleId;
    private long concertId;
    private LocalDate startDate;

    @Builder
    public static ScheduleCommand of(long scheduleId, long concertId, LocalDate startDate) {
        return new ScheduleCommand(scheduleId, concertId, startDate);
    }
}
