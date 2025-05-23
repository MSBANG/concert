package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ScheduleDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(force = true)
public class ScheduleResult {
    private final long scheduleId;
    private final Concert concert;
    private final LocalDate startDate;

    @Builder
    private ScheduleResult(long scheduleId, Concert concert, LocalDate startDate) {
        this.scheduleId = scheduleId;
        this.concert = concert;
        this.startDate = startDate;
    }

    public static ScheduleResult from(ConcertSchedule schedule) {
        return ScheduleResult.builder()
                .scheduleId(schedule.getScheduleId())
                .concert(schedule.getConcert())
                .startDate(schedule.getStartDate())
                .build();
    }

    public static ScheduleResult from(ScheduleDTO scheduleDTO) {
        return ScheduleResult.builder()
                .scheduleId(scheduleDTO.getScheduleId())
                .startDate(scheduleDTO.getStartDate())
                .build();
    }

}
