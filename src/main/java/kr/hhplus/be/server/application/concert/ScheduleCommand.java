package kr.hhplus.be.server.application.concert;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleCommand {
    private final long scheduleId;
    private final long concertId;
    private final LocalDate startDate;

    @Builder
    private ScheduleCommand(long scheduleId, long concertId, LocalDate startDate) {
        this.scheduleId = scheduleId;
        this.concertId = concertId;
        this.startDate = startDate;
    }

    public static ScheduleCommand fromScheduleId(long scheduleId){
        return ScheduleCommand.builder()
                .scheduleId(scheduleId)
                .build();
    }

    public static ScheduleCommand create(long scheduleId, long concertId, LocalDate startDate) {
        return ScheduleCommand.builder()
                .scheduleId(scheduleId)
                .concertId(concertId)
                .startDate(startDate)
                .build();
    }
}
