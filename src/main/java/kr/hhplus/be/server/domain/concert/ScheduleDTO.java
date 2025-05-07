package kr.hhplus.be.server.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScheduleDTO {
    private Long scheduleId;
    private LocalDate startDate;
}
